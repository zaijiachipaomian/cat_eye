package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.Movie;
import com.example.demo.entity.MovieRole;
import com.example.demo.entity.Type;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MovieService;
import com.example.demo.util.FileUpload;
import com.example.demo.controller.AdminHomeController.Response;

@RestController
@RequestMapping("/admin/films")
public class MovieControllerAdmin {
	
	@Autowired
	MovieService movieService;
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	TypeRepository typeRepository;
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	UserRepository userRepository;
	/**
	 * 电影的插入与更新，当有ID时为更新，没有ID时为插入，POST方式
	 * @param movie 要插入和更新的电影
	 * @param file  电影海报的文件
	 * @param files 电影图片的多个文件
	 * @return		如果插入或者更新成功，返回200,否则返回400
	 * @form 表单字段名称		类型			值（例子）
	 * 			id				text		1250342
	 * 			name			text		我们这一家we are
	 * 			area			text		印度尼西亚
	 * 			fileName(海报)	file		文件
	 * 			releaseDate		text		2018-12-25印度上映
	 * 			length			text		134分钟
	 * 			introduction	text		我们在这个屯立长大
	 * 			filesName(图片) 	file		文件		
	 * 			typeIds(电影类型的ID)text	1,2,3,4,5
	 * 			movieRoles		text	  [{ "personId" : 1 , "role" : "导演" },{ "personId" : 2 ,  "role" : "演员" }]
	 */
	@PostMapping("/add")
	public Response insertOrUpdateMovie(Movie movie ,
			@RequestParam(name = "fileName", required = false)  MultipartFile file,
			@RequestParam(name = "filesName", required = false) List<MultipartFile> files,
			@RequestParam(name = "typeIds", required = false) List<Long> typeIds,
			@RequestParam(name = "personRoles", required = false) String movieRoles ){
		System.out.println(movieRoles);
		movie.getMovieRoles().clear();
		movie.getPersons().clear();
		movie.getPhotos().clear();
		if(typeIds != null) {
			List<Type> types = typeRepository.findAllById(typeIds);
			movie.setTypes(types);
		}
		if(movieRoles != null) {
			List<MovieRole> movieRoleList = JSON.parseArray(movieRoles, MovieRole.class);
			System.out.println(movieRoleList);
			for(MovieRole movieRole : movieRoleList) {
				movie.addMovieRole(movieRole);
				movie.addPerson(personRepository.findById(movieRole.getPersonId()).get());
			}
		}
		//批量文件上传成功，返回文件的路径加名字的List,否则返回null
		List<String> urlList = null;
		String result = null;
		if(files != null)
			urlList = FileUpload.multifileUpload(files);
		if(file != null)
			result = FileUpload.fileUpload(file);
		boolean insertResult = movieService.addMovie(movie, urlList ,result);
		if(insertResult)
			return new Response(200,"电影插入成功");
		else
			return new Response(400,"电影插入失败");
	}
	/**
	 * 通过ID删除电影
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public Object deleteMovie(@PathVariable Long id) {
		try {
			movieService.delMovie(id);
			return new Response(200, "删除电影成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(400, "删除电影失败");
		}
	}
	
	/**
	 * 通过ID查找
	 */
	@GetMapping("/get/{id}")
	public Object getMovie(@PathVariable Long id) {
		Movie movie = null;
		Map<String,Object> map = new HashMap<>();
		try {
			movie = movieService.findMovieById(id);
			System.out.println(movie);
			map.put("movie", movie);
			map.put("comments", movie.getComments());
			map.put("newsList", movie.getNewsList());
			map.put("photos", movie.getPhotos());
			map.put("movieRoles", movie.getMovieRoles());
			return new Response(200,map);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(400,"获取电影失败");
		}
	}
}
