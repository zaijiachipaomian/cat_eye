package com.example.demo.service;

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
		//判断号码是否已注册
		u=userRepository.findByPhone(user.getPhone());
		if(u==null) {
			users=userRepository.save(user);
		}
		return users;
	}

	@Override
	public void delUser(Long id) {
		// TODO Auto-generated method stub
		userRepository.deleteById(id);
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		User users=null;
		users=userRepository.save(user);
		return users;
	}

	@Override
	public User getUser(Long id) {
		// TODO Auto-generated method stub
		User user=null;
		user=userRepository.findById(id).get();
		return user;
	}

	@Override
	public User getUserByPhone(String phone) {
		// TODO Auto-generated method stub
		User user=null;
		user=userRepository.findByPhone(phone);
		return user;
	}

	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

}
