package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.controller.AdminHomeController.Response;
import com.example.demo.entity.Movie;
import com.example.demo.entity.News;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.util.FileUpload;

@Controller
@RequestMapping("/admin/news")
public class NewsController {
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	NewsRepository newsRepository;
	
	/**
	 * 插入或更新电影新闻
	 * 表单字段			类型			值（例子）
	 * movieId			Text		1
	 * name				Text		新闻名字
	 * url				Text		新闻详情链接
	 * fileName			FILE		新闻缩写图
	 * id				Text		可选（当ID有值时可以更新）
	 * @param movieId 插入新闻的电影ID
	 * @param news	插入的新闻
	 * @return	Response
	 */
	@ResponseBody
	@PostMapping("/add")
	public Object insertOrUpdateNews(Long movieId , 
									 News news ,
									 @RequestParam(name = "fileName", required = false)  MultipartFile file) {
		System.out.println(movieId);
		System.out.println(news.toString());
		try {
			String picUrl = FileUpload.fileUpload(file);
			if(!picUrl.equals("false"))
				news.setPic(picUrl);
			Movie movie = movieRepository.findById(movieId).get();
			movie.addNews(news);
			movieRepository.save(movie);
			return new Response(200 , "插入或更新电影新闻成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400 , "插入或更新电影新闻失败");
		}
	}

	
	@ResponseBody
	@DeleteMapping("/delete/{id}")
	public Object deleteNews(@PathVariable Long id) {
		try {
			newsRepository.deleteById(id);
			return new Response(200 , "删除电影新闻成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400 , "删除电影新闻失败");
		}
	}
	
	@ResponseBody
	@GetMapping("/get/list")
	public Object getNewsList(@PageableDefault(page=0,size=10) Pageable pageable) {
		try {
			Page<News> newsList = newsRepository.findAll(pageable);
			return new Response(200 , newsList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400 , "删除电影新闻失败");
		}
	}
	
}
	