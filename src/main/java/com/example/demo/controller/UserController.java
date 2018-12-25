package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
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
	
	@GetMapping("/reg")
	public String register(){
		return "register";
	}
	
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
	
	//个人主页
	@GetMapping("me")
	public ModelAndView getMe(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("me");
		HttpSession session=request.getSession();
		User user=null;
		user=(User) session.getAttribute("user");
		modelAndView.addObject("user", user);
		return modelAndView;
	}
	
	@GetMapping("loginOut")
	public ModelAndView loginOut() {
		ModelAndView modelAndView=new ModelAndView("index");
		
		return modelAndView;
	}
	
	@GetMapping("/test1")
	public ModelAndView test1() {
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("key", 12345);
		return modelAndView;
	}
}
