package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@GetMapping("/login")
	public Map<String, User> login(String phone,String password) {
		User user=null;
		Map<String,User> map=new HashMap<String, User>();
		user=userService.login(phone,password);
		if(user!=null) {
			map.put("user", user);
		}
		return map;
	}
	
	@GetMapping("/register")
	public Map<String ,User> register(User user){
		Map<String,User> map=new HashMap<String,User>();
		User users=null;
		users=userService.register(user);
		if(users!=null) {
			map.put("users", users);
		}
		return map;
	}
	
	@GetMapping("/test")
	public String test() {
		return "index";
	}
}
