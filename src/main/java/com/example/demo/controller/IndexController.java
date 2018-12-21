package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.TodayBoxOffice;
import com.example.demo.service.BoxOfficeService;

@RestController
public class IndexController {
	
	@Autowired
	BoxOfficeService boxOfficeService;
	
	@GetMapping("/")
	public Map<String, Object> getIndexInfor() {
		Map<String , Object> map = new HashMap<String , Object>();
		List<TodayBoxOffice> todayBoxOffice = boxOfficeService.getTodayBoxOffice();
		map.put("todayBoxOffice", todayBoxOffice);
		return map;
	}
}
