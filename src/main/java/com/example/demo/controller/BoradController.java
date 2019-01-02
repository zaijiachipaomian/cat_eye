package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.AdminHomeController.Response;
import com.example.demo.entity.Movie;
import com.example.demo.service.BoardSerivce;
import com.example.demo.service.MovieService;

@RestController
@RequestMapping("/board")
public class BoradController {
	
	@Autowired
	MovieService movieService; 
	
	@Autowired
	BoardSerivce boardSerivce;
	
	/**
	 * 热映榜单
	 * @param pageable
	 * @return
	 */
	@GetMapping("/7/api")
	public Object getReleasingBoard(@PageableDefault(page=0,size=10)Pageable pageable) {
		try {
			Page<Movie> releasingBoard = boardSerivce.getReleasingBoard(pageable);
			return new Response(200, releasingBoard);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400, "获取热映榜单电影失败");
		}
	}
	
	/**
	 * 历史榜单
	 * @param pageable
	 * @return
	 */
	@GetMapping("/4/api")
	public Object getTop100Board(@PageableDefault(page=0,size=10)Pageable pageable) {
		try {
			Page<Movie> top100Board = boardSerivce.getTop100Board(pageable);
			return new Response(200, top100Board);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400, "获取历史榜单电影失败");
		}
	}
	
	/**
	 * 国内票房榜单
	 * @param pageable
	 * @return
	 */
	@GetMapping("/1/api")
	public Object getBoxOfficeBoard(@PageableDefault(page=0,size=10)Pageable pageable) {
		try {
			List<Map<String, Object>> boxOfficeBoardList = boardSerivce.getBoxOfficeBoard(pageable);
			return new Response(200, boxOfficeBoardList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400, "获取国内票房榜单电影失败");
		}
	}
}
