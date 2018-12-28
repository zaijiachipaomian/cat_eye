package com.example.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.example.demo.controller.AdminHomeController.Response;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	
	private static String valid_code=null;
	
	//个人主页
	@ResponseBody
	@GetMapping("/{id}")
	public Object getMe(HttpServletRequest request,@PathVariable Long id) {
		HttpSession session=request.getSession();
		Response re=new Response();
		User user=(User) session.getAttribute("user");
		if(user!=null) {
			//判断该用户是否为当前登录用户
			if(user.getId()==id) {
				re.setCode(200);
				re.setData(user);
				return re;
			}
			else{
				re.setCode(400);
				re.setData("用户错误");
				return re;
			}
		}
		else {
			re.setCode(400);
			re.setData("用户未登录");
			return re;
		}
	}
	
	//获取登陆页面
	@GetMapping("/login")
	public String login(HttpServletRequest request) {
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		//若用户已登录则回到主页
		if(user!=null) {
			return "index";
		}
		else{
			return "login";
		}
	}
	
	//用户登陆
	@ResponseBody
	@PostMapping("/login")
	public Object login(HttpServletRequest request,String phone,String password,String yzm) {
		System.out.println("正确的验证码："+valid_code);
		System.out.println("用户输入的验证码："+yzm);
		System.out.println("phone="+phone);
		System.out.println("password="+password);
		Response re=new Response();
		User user=null;
		//用户使用密码登录
		if(yzm==null) {
			user=userService.login(phone,password);
		}
		//用户使用验证码登录
		else{
			if(valid_code!=null) {
				if(valid_code.equals(yzm)) {
					if(phone==null||phone.equals("")) {
						re.setCode(400);
						re.setData("手机号为空");
						return re;
					}
					else {
						user=userService.getUserByPhone(phone);
					}
				}
				//判断该号码是否已注册 若没有注册 则跳转到注册页面
				if(user==null) {
					re.setCode(400);
					re.setData("用户未注册,请先注册再登陆");
					return re;
				}
			}
			else {
				re.setCode(400);
				re.setData("未获取验证码");
				return re;
			}
		}
		if(user!=null) {
			HttpSession session=request.getSession();
			session.setAttribute("user", user);
			re.code=200;
			re.data="登陆成功";
			return re;
		}
		else {
			
			if(yzm==null) {
				re.setCode(400);
				re.setData("手机号码或密码错误");
			}
			else {
				re.setCode(400);
				re.setData("验证码错误");
			}		
			return re;
		}
	}
	
	//获取注册页面
	@GetMapping("/reg")
	public String register(){
		return "register";
	}
	
	//用户注册
	@ResponseBody
	@PostMapping("/reg")
	public Object register(HttpServletRequest request,User user,String yzm) {
		System.out.println("正确的验证码："+valid_code);
		System.out.println("用户输入的验证码："+yzm);
		Response re=new Response();
		//判断验证码是否正确
		if(valid_code!=null) {
			if(valid_code.equals(yzm)) {
				//判断手机号码是否已注册
				if(userService.getUserByPhone(user.getPhone())!=null) {
					re.setCode(400);
					re.setData("手机号已注册");
					return re;
				}
				//判断用户名是否已已存在
				else if(userService.getUserByUsername(user.getUsername())!=null) {
					re.setCode(400);
					re.setData("用户名已存在");
					return re;
				}
				else {
					user.setRegDate(new Date());
					if(userService.register(user)!=null) {
						re.setCode(200);
						re.setData("注册成功");
						return re;
					}
					else {
						re.setCode(400);
						re.setData("注册失败");
						return re;
					}
				}
				
			}
			else{
				re.code=400;
				re.data="验证码错误";
				return re;
			}
		}
		else{
			re.code=400;
			re.data="未获取验证码";
			return re;
		}
		
		
	}
	
	@GetMapping("/loginOut")
	public String loginOut(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.removeAttribute("user");
		return "index";
	}
	
	//跳转到修改信息界面
	@ResponseBody
	@GetMapping("/modify/{id}")
	public Object updateUser(HttpServletRequest request,Long id) {
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		Response re=new Response();
		if(user!=null) {
			if(user.getId()==id) {
				re.code=200;
				re.data="正确";
				return re;
			}
			else {
				re.code=400;
				re.data="错误的修改";
				return re;
			}
		}
		else {
			re.code=400;
			re.data="用户未登录";
			return re;
		}
	}
	
	//用户修改个人信息
	@ResponseBody
	@PostMapping("/modify")
	public Object updateUser(HttpServletRequest request,User user) {
		HttpSession session=request.getSession();
		User u=(User) session.getAttribute("user");
		Response re=new Response();
		//判断是否当前用户
		if(u.getId()==user.getId()) {
			u=null;
			u=userRepository.save(user);
			if(u!=null) {
				re.code=200;
				re.data="修改成功";
				return re;
			}
			else {
				re.code=400;
				re.data="修改失败";
				return re;
			}
		}
		else {
			re.code=400;
			re.data="用户错误";
			return re;
		}
	}
	
	//获取验证码
	@GetMapping("/valid_code")
    @ResponseBody
    public Object ReceiverSMS(String phone) {
		System.out.println(phone); 
		User user=null;
		Response re=new Response();
		user=userService.getUserByPhone(phone);
		if(user==null) {
			re.code=400;
			re.data="用户未注册！";
			return re;
		}
		 
        int appid = 1400155268; 
        String appkey = "9e7873c0a1b3bc1fb8976cfcafb1599e";
        // 需要发送短信的手机号码
        String[] phoneNumbers = {phone};
        // 短信模板ID，需要在短信应用中申请
        int templateId = 219697; 
        // 签名
        String smsSign = "池章立个人技术经验分享"; 
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 9).toLowerCase();
        System.out.println(uuid);

        //User user = userRepository.findByPhoneNumber(phone);
        //System.out.println(user);
        
        stringRedisTemplate.opsForValue().set(phone, uuid,1,TimeUnit.MINUTES);
        //发送短信
        try {
            String[] params = {uuid,"1"};
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            //send 的参数 1. 0 : 代表普通参数， 2. countrycode 国家代码， 3. 短信的手机号  4. 短信发送的内容， 5和6  扩展内容
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers[0],
                templateId, params, smsSign, "", "");  
            System.out.println(result);
            valid_code=uuid;
        } catch (Exception e) {
        	re.code=400;
			re.data="获取验证码出错";
			return re;
        }
        //用于回调
        re.code=200;
		re.data="获取验证码成功";
		return re;
    }
}
