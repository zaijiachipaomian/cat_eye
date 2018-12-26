package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.User;

public interface UserService {
	public User login(String phone,String password);
	public User register(User user);
	public User getUser(Long id);
	public User getUserByPhone(String phone);
	public void delUser(Long id);
	public User updateUser(User user);
}
