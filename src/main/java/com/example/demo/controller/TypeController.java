package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Type;
import com.example.demo.repository.TypeRepository;

@RestController
@RequestMapping("/type")
public class TypeController {
	
	@Autowired
	TypeRepository typeRepository;
	
	@GetMapping("/list")
	public List<Type> getTypeList(){
		return typeRepository.findAll();
	}
	
}
