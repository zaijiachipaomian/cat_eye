package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;
import com.example.demo.entity.MovieRole;
import com.example.demo.entity.Person;
import com.example.demo.entity.Type;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.service.MovieService;

@RequestMapping("/films")
@RestController
public class MovieController {
	
	@Autowired
	MovieService movieService;
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	TypeRepository typeRepository;
	
	@Autowired
	PersonRepository personRepository;
	/**
	 * 电影详情页完成
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Map<String,Object> getMovie(@PathVariable Long id) {
		Map<String,Object> map = new HashMap<>();
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
		//List<Person> persons = new ArrayList<>();
//		for(MovieRole movieRole : movie.getMovieRoles()) {
//			Person person = personRepository.findById(movieRole.getPersonId()).get();
//			persons.add(person);
//		}
		//movie.setPersons(persons);
		map.put("movie",movie);
		return map;
	}
}
