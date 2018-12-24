package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
	public User login(String phone,String password);
	public User register(User user);
}
