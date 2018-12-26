package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.TodayBoxOffice;
import com.example.demo.service.BoxOfficeService;

@Controller
public class IndexController {
	
	@Autowired
	BoxOfficeService boxOfficeService;
	
	@ResponseBody
	@GetMapping("/index_data/todayBoxOffice")
	public Map<String, Object> getIndexInfor() {
		Map<String , Object> map = new HashMap<String , Object>();
		List<TodayBoxOffice> todayBoxOffice = boxOfficeService.getTodayBoxOffice();
		map.put("todayBoxOffice", todayBoxOffice);
		return map;
	}
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
}
