package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
public class BoradController {
	//票房排名
	@GetMapping("/1")
	public Map<String , Object> getBoardBoxOffice() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "123456");
		map.put("name", "haiwang");
		return map;
	}
	
	//
}
