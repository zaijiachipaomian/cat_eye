package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;
import com.example.demo.entity.MovieRole;
import com.example.demo.entity.News;
import com.example.demo.entity.Person;
import com.example.demo.entity.Photo;
import com.example.demo.entity.Type;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.service.MovieService;
import com.example.demo.service.PersonService;
import com.example.demo.util.FileUpload;

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
	/**
	 * 电影的插入与更新，当有ID时为更新，没有ID时为插入，POST方式
	 * @param movie 要插入和更新的电影
	 * @param file  电影海报的文件
	 * @param files 电影图片的多个文件
	 * @return		如果插入或者更新成功，返回true,否则返回false
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
	 * 			personRoles		text	  [{ "personId" : 1 , "role" : "导演" },{ "personId" : 2 ,  "role" : "演员" }]
	 */
	@PostMapping("/add")
	public String insertOrUpdateMovie(Movie movie ,
			@RequestParam("fileName")  MultipartFile file,
			@RequestParam("filesName") List<MultipartFile> files,
			@RequestParam("typeIds") List<Long> typeIds,
			@RequestParam("personRoles") String movieRoles ){
		if(typeIds != null) {
			List<Type> types = typeRepository.findAllById(typeIds);
			movie.setTypes(types);
		}
		List<MovieRole> movieRoleList = JSON.parseArray(movieRoles, MovieRole.class);
		System.out.println(movieRoleList);
		for(MovieRole movieRole : movieRoleList) {
			movie.addMovieRole(movieRole);
			movie.addPerson(personRepository.findById(movieRole.getPersonId()).get());
		}
		
		
		//批量文件上传成功，返回文件的路径加名字的List,否则返回null
		List<String> urlList = FileUpload.multifileUpload(files);
		String result = FileUpload.fileUpload(file);
		if(urlList == null || result.equals("false"))
			return "false";
		boolean insertResult = movieService.addMovie(movie, urlList ,result);
		if(insertResult)
			return "true";
		else
			return "false";
	}
	/**
	 * 通过ID删除电影
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public String deleteMovie(@PathVariable Long id) {
		try {
			movieService.delMovie(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
		return "true";
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
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
		return map;
	}
	
	@GetMapping("/test/{id}")
	public Object getMovie2(@PathVariable Long id) {
		Map<String,Object> map = new HashMap<>();
//		Long[] ids = typeRepository.findTypeIdByMovieId(id);
//		map.put("data",typeRepository.findTypeByTypeId(ids));
//		
//		Long[] ids2 = movieRepository.findMovidIdByTypeId(1L);
//		map.put("data2",movieRepository.findMovieByMovieId(ids2));
		
		return map;
	}
	
	@GetMapping("/")
	@ResponseBody
	public Page<Movie> getMovieList(String movieName , @PageableDefault(page=0,size=10)Pageable pageable){
		//Page<Movie> moviePage = movieRepository.findAll(pageable);
		Page<Movie> moviePage = movieRepository.findByTypesId(1L, pageable);
		return moviePage;
	}
	
	
	
	
	
}