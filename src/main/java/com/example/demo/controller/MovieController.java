package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;
import com.example.demo.entity.MovieQuery;
import com.example.demo.service.MovieService;
import com.example.demo.controller.AdminHomeController.Response;
@RequestMapping("/films")
@RestController
public class MovieController {
	
	@Autowired
	MovieService movieService;

	/**
	 * 电影详情页完成
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}/api")
	public Object getMovie(@PathVariable Long id) {
		Map<String,Object> map = new HashMap<>();
		try {
			Movie movie = movieService.findMovieById(id);
			//类型
			//图集
			map.put("photos",movie.getPhotos());
			//资讯
			map.put("news",movie.getNewsList());
			//演职人员
			map.put("roles",movie.getMovieRoles());
			//热门短评
			List<Comment> comments = movie.getComments();
			map.put("comments",comments);
			//电影评分
			Double avg = comments.stream().collect(Collectors.averagingDouble(Comment::getScore));
			map.put("score",avg);
			//电影人
			map.put("movie",movie);
			return map;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}
	
	@GetMapping("/area/list")
	public Object getAreaSet(){
		try {
			Set<String> areaSet = movieService.findDistinctAreaSet();
			return new Response(200,areaSet);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400,"获取地域列表失败");
		}
	}
	
	@GetMapping("/release/list")
	public Object getReleaseSet(){
		try {
			Set<String> releaseDateSet = movieService.findDistinctReleaseSet();
			return new Response(200,releaseDateSet);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400,"获取上映年份列表失败");
		}
	}
	
	
	/**
	 * 	前台的筛选参数
	 * 	showType(1: 【电影正在热映】过去两个月内上映的电影 ；
	 *  		 2:【电影即将上映】还没有上映的电影
	 *  	     3:【经典影片】 已经上映超过两个月的电影）   
	 *  sortId
	 *  		（1：【按热门排序】 假功能，不存在的；
	 *  		  2: 【按时间排序】   按上映时间倒序排序
	 *  		  3:【按评价排序】   按电影评分倒序排序
	 *  typeId
	 *  		（按电影类型的ID来进行筛选）
	 *  area
	 *  		（按电影的地域进行筛选）
	 *  releaseDate
	 *  		（按电影的上映日期进行筛选）
	 *  比如我要查看：正在热映(showType=1)  且  类型为剧情（TYPEID=1） 且  地域为大陆（area=大陆） 且  上映日期为2018年（releaseDate=2018）
	 *   就这么写： ip:port/flims?showType=1&sortId=3&typeId=1&area=大陆&releaseDate=2018
	 * @param movieQuery
	 * @param pageable
	 * @return
	 */
	@GetMapping("/")
	@ResponseBody
	public Object getMovieList(MovieQuery movieQuery , @PageableDefault(page=0,size=10)Pageable pageable){
		try {
			System.out.println(movieQuery.toString());
			Page<Movie> movieList = movieService.getMovieList(movieQuery, pageable);
			return new Response(200, movieList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400, "获取筛选电影列表失败");
		}
	}
}	
