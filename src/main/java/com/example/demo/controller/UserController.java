package com.example.demo.controller;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.web.PageableDefault;
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
import com.example.demo.controller.AdminHomeController.Admin;
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
	/**
	 * 请求方式Post
	 * 需要两个参数，参数名为phone,password
	 * 
	 */
	//用户登陆
	@ResponseBody
	@PostMapping("/login")
	public Object login(HttpServletRequest request,HttpServletResponse response,String phone,String password) {
		System.out.println("phone="+phone);
		System.out.println("password="+password);
		Response re=new Response();
		User user=null;
		//用户使用密码登录
		user=userService.login(phone,password);
		if(user!=null) {
			HttpSession session=request.getSession();
			session.setAttribute("user", user);
			
			//设置两个cookie，user_id代表用户id,username代表用户名
			Cookie c=new Cookie("user_id",user.getId().toString());
			Cookie c1=new Cookie("username", user.getUsername());
			//会话级cookie，关闭浏览器失效
			c.setMaxAge(-1);
			c1.setMaxAge(-1);
			c.setPath("/");
			c1.setPath("/");
			response.addCookie(c);
			response.addCookie(c1);
			re.setCode(200);
			re.setData("登陆成功");
			return re;
		}
		else{
			re.setCode(400);
			re.setData("用户名或密码错误");
			return re;
		}
	}
	
	//获取注册页面
	@GetMapping("/reg")
	public String register(){
		return "register";
	}
	
	
	/**
	 * 请求方式Post
	 * 需要一个参数，参数名为yzm，表示用户输入的验证码
	 */
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
					//设置用户注册的时间
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
				re.setCode(400);
				re.setData("验证码错误");
				return re;
			}
		}
		else{
			re.setCode(400);
			re.setData("未获取验证码");
			return re;
		}
		
		
	}
	
	//退出登陆 删除cookie并remove user的Session
	@GetMapping("/loginOut")
	public String loginOut(HttpServletRequest request,HttpServletResponse response) {
		HttpSession session=request.getSession();
		session.removeAttribute("user");
		
		//删除登陆时的cookie
		Cookie killMyCookie = new Cookie("user_id", null);
        killMyCookie.setMaxAge(0);
        killMyCookie.setPath("/");
        response.addCookie(killMyCookie);
        Cookie killMyCookie1 = new Cookie("username", null);
        killMyCookie1.setMaxAge(0);
        killMyCookie1.setPath("/");
        response.addCookie(killMyCookie1);
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
				re.setCode(200);
				re.setData("正确");
				return re;
			}
			else {
				re.setCode(400);
				re.setData("错误的修改");
				return re;
			}
		}
		else {
			re.setCode(400);
			re.setData("用户未登录");
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
				re.setCode(200);
				re.setData("修改成功");
				return re;
			}
			else {
				re.setCode(400);
				re.setData("修改失败");
				return re;
			}
		}
		else {
			re.setCode(400);
			re.setData("用户错误");
			return re;
		}
	}
	
	
	/**
	 * 请求方式Get
	 * 需要一个参数，参数名为phone，表示收验证码的手机号码
	 */
	//注册时获取验证码
	@GetMapping("/valid_code_reg")
    @ResponseBody
    public Object ReceiverSMS1(String phone) {
		System.out.println(phone); 
		User user=null;
		Response re=new Response();
		user=userService.getUserByPhone(phone);
		if(user!=null) {
			re.setCode(400);
			re.setData("该号码已注册");
			return re;
		}
		else {
			int appid = 1400155268; 
	        String appkey = "9e7873c0a1b3bc1fb8976cfcafb1599e";
	        // 需要发送短信的手机号码
	        String[] phoneNumbers = {phone};
	        // 短信模板ID，需要在短信应用中申请
	        int templateId = 219697; 
	        // 签名
	        String smsSign = "池章立个人技术经验分享"; 
	        Integer random=(int)((Math.random()*9+1)*100000);
	        String uuid =random.toString();
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
	        	re.setCode(400);
				re.setData("获取验证码出错");
				return re;
	        }
	        //用于回调
	        re.setCode(200);
			re.setData("获取验证码成功");
			return re;
		}
    }
	
	//找回密码时获取验证码
	@GetMapping("/valid_code_back")
    @ResponseBody
    public Object ReceiverSMS2(String phone) {
		System.out.println(phone); 
		User user=null;
		Response re=new Response();
		user=userService.getUserByPhone(phone);
		if(user==null) {
			re.setCode(400);
			re.setData("该号码未注册");
			return re;
		}
		else {
			int appid = 1400155268; 
	        String appkey = "9e7873c0a1b3bc1fb8976cfcafb1599e";
	        // 需要发送短信的手机号码
	        String[] phoneNumbers = {phone};
	        // 短信模板ID，需要在短信应用中申请
	        int templateId = 219697; 
	        // 签名
	        String smsSign = "池章立个人技术经验分享"; 
	        Integer random=(int)((Math.random()*9+1)*100000);
	        String uuid =random.toString();
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
	        	re.setCode(400);
				re.setData("获取验证码出错");
				return re;
	        }
	        //用于回调
	        re.setCode(200);
			re.setData("获取验证码成功");
			return re;
		}
    }
	
	/**
	 * 请求方式Get
	 * @param id User表的数据id
	 * 需要一个参数，参数名为statu，表示修改用户后的用户状态
	 */
	//管理员修改用户状态
	@GetMapping("/admin/modify/{id}")
    @ResponseBody
    public Object updateUserStatu(HttpServletRequest request,@PathVariable Long id,String statu) {
		HttpSession session=request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		Response re=new Response();
		if(admin!=null) {
			User user=userService.getUser(id);
			if(user!=null) {
				user.setStatu(statu);
				if(userService.updateUser(user)!=null) {
					re.setCode(200);
					re.setData("修改成功");
					return re;
				}
				else {
					re.setCode(400);
					re.setData("修改失败");
					return re;
				}
				
			}
			else {
				re.setCode(400);
				re.setData("用户不存在");
				return re;
			}
			
		}
		else {
			re.setCode(400);
			re.setData("管理员未登录");
			return re;
		}
	}
	
	//管理员删除用户通过ID
	@GetMapping("/admin/delete/{id}")
	@ResponseBody
	public Object deleteById(HttpServletRequest request,@PathVariable Long id) {
		HttpSession session=request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		Response re=new Response();
		if(admin!=null) {
			User user=userService.getUser(id);
			if(user!=null) {
				try{
					userService.delUser(id);
					re.setCode(200);
					re.setData("删除成功");
					return re;
				}catch (Exception e) {
					e.printStackTrace();
					re.setCode(400);
					re.setData("删除失败");
					return re;
				}
				
			}
			
			else {
				re.setCode(400);
				re.setData("用户不存在");
				return re;
			}
		}
		
		else {
			re.setCode(400);
			re.setData("管理员未登录");
			return re;
		}
	}
	
	
	//管理员查看所有用户 分页默认每页10个用户
	@GetMapping("/admin/get")
	@ResponseBody
	public Object getAllUser(HttpServletRequest request,@PageableDefault(page=0,size=10)Pageable pageable) {
		HttpSession session=request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		Page<User> ls=null;
		Response re=new Response();
		if(admin!=null) {
			ls=userService.getAllUser(pageable);
			if(ls!=null) {
				re.setCode(200);
				re.setData(ls);
				return re;
			}
			else {
				re.setCode(400);
				re.setData("获取用户列表失败");
				return re;
			}
		}
		else {
			re.setCode(400);
			re.setData("管理员未登录");
			return re;
		}
	}
	
	
	//管理员增加管理员通过用户ID
	@GetMapping("/admin/add/{id}")
	@ResponseBody
	public Object addAdmin(HttpServletRequest request,@PathVariable Long id) {
		HttpSession session=request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		Response re=new Response();
		if(admin!=null) {
			User user=userService.getUser(id);
			if(user!=null) {
				user.setStatu("管理员");
				if(userService.updateUser(user)!=null) {
					re.setCode(200);
					re.setData("修改成功");
					return re;
				}
				else {
					re.setCode(400);
					re.setData("修改失败");
					return re;
				}
				
			}
			else {
				re.setCode(400);
				re.setData("用户不存在");
				return re;
			}
			
		}
		else {
			re.setCode(400);
			re.setData("管理员未登录");
			return re;
		}
	}
}
