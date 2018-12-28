package com.example.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.User;

public interface UserService {
	public User login(String phone,String password);
	public User register(User user);
	public Page<User> getAllUser(Pageable pageable);
	public User getUser(Long id);
	public User getUserByUsername(String username);
	public User getUserByPhone(String phone);
	public void delUser(Long id);
	public User updateUser(User user);
}
