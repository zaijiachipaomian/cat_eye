package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	//通过手机号查找用户
	public User findByPhone(String phone);
	//用户登录
	public User findByPhoneAndPassword(String phone,String password);
}
