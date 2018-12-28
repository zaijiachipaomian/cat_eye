package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;
import com.example.demo.entity.MovieQuery;
import com.example.demo.entity.MovieRole;
import com.example.demo.entity.Person;
import com.example.demo.entity.Type;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.service.MovieService;
import com.example.demo.util.StringUitl;

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
	
	@GetMapping("/area/list")
	public Set<String> getAreaSet(){
		Set<String> areaSet = movieRepository.findDistinctAreaSet();
		Set<String> dotSet = areaSet.stream().filter(s -> s.contains(",")).collect(Collectors.toSet());
		for(String s : dotSet) {
			String[] split = s.split(",");
			for(String tempStr : split) {
				areaSet.add(tempStr);
			}
			areaSet.remove(s);
		}
		return areaSet;
	}
	
	@GetMapping("/release/list")
	public Set<String> getReleaseSet(){
		Set<String> releaseDateSet = movieRepository.findDistinctReleaseSet();
		releaseDateSet.remove(null);
		return releaseDateSet;
	}
	
	@GetMapping("/")
	@ResponseBody
	public Page<Movie> getMovieList(MovieQuery movieQuery , @PageableDefault(page=0,size=10)Pageable pageable){
		
		return movieRepository.findAll(new Specification<Movie>() {
			@Override
			public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if(!"".equals(movieQuery.getTypeId()) && (movieQuery.getTypeId() != null)) {
					Join join = root.join("types");
					predicates.add(
							criteriaBuilder.equal(join.get("id"),movieQuery.getTypeId())
							);
				}
				if(!"".equals(movieQuery.getName()) && (movieQuery.getName() != null)) {
					predicates.add(criteriaBuilder.like(root.<String>get("name"), "%"+movieQuery.getName()+"%"));
				}
				if(!"".equals(movieQuery.getArea()) && (movieQuery.getArea() != null)) {
					predicates.add(criteriaBuilder.like(root.<String>get("area"), "%"+movieQuery.getArea()+"%"));
				}
				if(!"".equals(movieQuery.getReleaseDate()) && (movieQuery.getReleaseDate() != null)) {
					Integer year = Integer.parseInt(movieQuery.getReleaseDate());
					predicates.add(criteriaBuilder.greaterThan(root.get("releaseDate").as(String.class), year + "-01-01"));
					predicates.add(criteriaBuilder.lessThan(root.get("releaseDate").as(String.class), (year + 1 + "")));
				}
				query.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(criteriaBuilder.asc(root.get("releaseDate")));
				//Join join2 = root.join("comments");
				//query.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(criteriaBuilder.asc(join2.get("score")));
			
			
				
				
                return null;
				// TODO Auto-generated method stub
//				Join join = root.join("types");
//				return criteriaBuilder.equal(join.get("id"),typeId);
			}
		},pageable);
	}
}	
