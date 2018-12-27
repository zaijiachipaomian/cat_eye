package com.example.demo.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

class Response{
	int code;
	String data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}

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
	@GetMapping("")
	public String getMe(HttpServletRequest request) {
		//TODO
		return "login";
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
	public Map<String,Object> login(HttpServletRequest request,String phone,String password,String yzm) {
		Map<String,Object> map=new HashMap<String,Object>();
		System.out.println("正确的验证码："+valid_code);
		System.out.println("用户输入的验证码："+yzm);
		System.out.println("phone="+phone);
		System.out.println("password="+password);
		System.out.println("yzm="+yzm);
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
					user=userService.getUserByPhone(phone);
				}
				//判断该号码是否已注册 若没有注册 则跳转到注册页面
				if(user==null) {
					re.setCode(400);
					re.setData("用户未注册,请先注册再登陆");
					map.put("response", re);
					return map;
				}
			}
			else {
				re.setCode(400);
				re.setData("未获取验证码");
				map.put("response", re);
				return map;
			}
		}
		if(user!=null) {
			HttpSession session=request.getSession();
			session.setAttribute("user", user);
			re.setCode(200);
			re.setData("登陆成功");
			map.put("response", re);
			return map;
		}
		else {
			
			if(yzm==null) {
				re.setData("手机号码或密码错误");
			}
			else {
				re.setData("验证码错误");
			}
			re.setCode(400);
			map.put("response", re);
			return map;
		}
	}
	
	//获取注册页面
	@GetMapping("/reg")
	public String register(){
		return "register";
	}
	
	//用户注册
	@PostMapping("/reg")
	public String register(HttpServletRequest request,User user,String yzm) {
		System.out.println("正确的验证码："+valid_code);
		System.out.println("用户输入的验证码："+yzm);
		//判断验证码是否正确
		if(valid_code!=null) {
			if(valid_code.equals(yzm)) {
				if(userService.register(user)!=null) {
					request.setAttribute("message", "注册成功！");
					return "login";
				}
				else {
					request.setAttribute("message", "注册失败！");
					return "register";
				}
			}
			else{
				request.setAttribute("message", "验证码错误！");
				return "register";
			}
		}
		else{
			request.setAttribute("message", "未获取验证码！");
			return "register";
		}
		
		
	}
	
	@GetMapping("/loginOut")
	public String loginOut(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.removeAttribute("user");
		return "index";
	}
	
	//跳转到修改信息界面
	@GetMapping("/modify")
	public String updateUser() {
		//TODO
		return "updateUser";
	}
	
	//用户修改个人信息
	@PostMapping("/modify")
	public String updateUser(HttpServletRequest request,User user) {
		//TODO
		HttpSession session=request.getSession();
		User u=(User) session.getAttribute("user");
		//判断是否当前用户
		if(u.getId()==user.getId()) {
			u=null;
			u=userRepository.save(user);
			if(u!=null) {
				//TODO
				return "true";
			}
		}
		return "updateUser";
	}
	
	//获取验证码
	@PostMapping("/valid_code")
    @ResponseBody
    public String ReceiverSMS(String phone) {
		phone="13528099183";
		System.out.println(phone);  
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
            return "fail";
        }
        //用于回调
        return phone;
    }
}
