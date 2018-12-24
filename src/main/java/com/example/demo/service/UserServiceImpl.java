package com.example.demo.service;

import java.nio.file.attribute.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	@Override
	public User login(String phone, String password) {
		// TODO Auto-generated method stub
		User user=null;
		user=userRepository.findByPhoneAndPassword(phone, password);
		return user;
	}

	@Override
	public User register(User user) {
		// TODO Auto-generated method stub
		User users=null;
		User u=null;
		u=userRepository.findByPhone(user.getPhone());
		if(u==null) {
			users=userRepository.save(user);
		}
		return users;
	}

}