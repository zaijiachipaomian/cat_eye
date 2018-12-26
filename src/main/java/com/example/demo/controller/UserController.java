package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	
	//个人主页
	@GetMapping("")
	public String getMe(HttpServletRequest request) {
		return "login";
	}
	
	//获取登陆页面
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	//用户登陆
	@PostMapping("/login")
	public String login(HttpServletRequest request,String phone,String password,String yzm) {
		User user=null;
		user=userService.login(phone, password);
		if(user!=null) {
			HttpSession session=request.getSession();
			session.setAttribute("user", user);
			return "index";
		}
		else {
			request.setAttribute("message", "用户名或密码错误!");
			return "login";
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
		if(userService.register(user)!=null) {
			request.setAttribute("message", "注册成功！");
			return "login";
		}
		else {
			request.setAttribute("message", "注册失败！");
			return "register";
		}
	}
	
	
	
	@GetMapping("loginOut")
	public String loginOut(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.removeAttribute("user");
		return "index";
	}
	
}
