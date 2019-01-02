package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Movie;
import com.example.demo.entity.MovieQuery;
import com.example.demo.service.BoardSerivce;
import com.example.demo.service.BoxOfficeService;
import com.example.demo.service.MovieService;
import com.example.demo.util.MaoYanBoxOfficeApi;
import com.example.demo.controller.AdminHomeController.Response;
@Controller
public class IndexController {
	
	@Autowired
	BoxOfficeService boxOfficeService;
	
	@Autowired
	MovieService movieService;
	
	@Autowired
	BoardSerivce boardSerivce;
	
	@ResponseBody
	@GetMapping("/index_data/todayBoxOffice")
	public Object getTodayBoxOffice() {
		org.jsoup.Connection.Response resp = null;
		try {
			resp = MaoYanBoxOfficeApi.getMaoYanBoxOfficeApi();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resp.body();
	}
	/**
	 * 主页显示8部正在热映的电影，并按照今日票房的数量进行排序，并列出正在热映的电影数量。
	 * @return
	 */
	@ResponseBody
	@GetMapping("/index_data/releasing")
	public Object getReleasing() {
		HashMap<String, Object> map = new HashMap<>();
		try {
			List<Map<String, Object>> indexReleasing = movieService.getReleasingMovieOrderByBoxOfficeLimit8();
			Long releasingCount = movieService.getReleasingCount();
			map.put("movie", indexReleasing);
			map.put("count", releasingCount);
			return new Response(400,map);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400,"首页获取8部正在热映电影失败");
		}
	}
	/**
	 * 返回即将上映的8部电影，还有即将上映的电影数量。
	 * @return
	 */
	@ResponseBody
	@GetMapping("/index_data/comingSoon")
	public Object getComingSoon() {
		HashMap<String, Object> map = new HashMap<>();
		try {
			MovieQuery movieQuery = new MovieQuery();
			movieQuery.setShowType("2");
			movieQuery.setSortId("2");
			PageRequest pageRequest = new PageRequest(0, 8);
			Page<Movie> movieList = movieService.getMovieList(movieQuery, pageRequest);
			map.put("movie", movieList.getContent());
			map.put("count", movieList.getTotalElements());
			return new Response(400,map);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400,"首页获取8部正在热映电影失败");
		}
	}
	
	/**
	 * 返回top100榜单到首页
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@GetMapping("/index_data/top100")
	public Object getTop100() {
		try {
			PageRequest pageRequest = new PageRequest(0, 10);
			Page<Movie> top100Board = boardSerivce.getTop100Board(pageRequest);
			return new Response(200, top100Board.getContent());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400,"首页获取10部top100电影失败");
		}
	}

	
	@GetMapping("/")
	public String index(HttpServletRequest request,HttpServletResponse response) {
		return "index";
	}
	
	

}
