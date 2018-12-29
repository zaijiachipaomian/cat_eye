package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	@GetMapping("/{id}/api")
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
		if(areaSet.contains(null))
			areaSet.remove(null);
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
		if(releaseDateSet.contains(null))
			releaseDateSet.remove(null);
		return releaseDateSet;
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
	public Page<Movie> getMovieList(MovieQuery movieQuery , @PageableDefault(page=0,size=10)Pageable pageable){
		System.out.println(movieQuery.toString());
		return movieRepository.findAll(new Specification<Movie>() {
			@Override
			public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				//标签筛选
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
				//导航栏筛选
				if(!"".equals(movieQuery.getShowType()) && (movieQuery.getShowType() != null)) {
					//Integer showType = Integer.parseInt(movieQuery.getShowType());
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Calendar c = Calendar.getInstance();
					Date date = new Date();
					c.setTime(date);
					//正在热映（过去两个月）
					if(movieQuery.getShowType().equals("1")) {
				        c.add(Calendar.MONTH, - 2);
				        Date d = c.getTime();
				        
				        System.out.println(format.format(d));
				        System.out.println(format.format(date));
				        System.out.println("正在热映");
				        predicates.add(criteriaBuilder.greaterThan(root.get("releaseDate").as(String.class), format.format(d)));
						predicates.add(criteriaBuilder.lessThan(root.get("releaseDate").as(String.class), format.format(date)));
					}
					//即将上映
					if(movieQuery.getShowType().equals("2")) {
						c.add(Calendar.DATE, + 1);
						Date d = c.getTime();
						predicates.add(criteriaBuilder.greaterThan(root.get("releaseDate").as(String.class), format.format(d)));
					}
					//经典电影（上映过了两个月）
					if(movieQuery.getShowType().equals("3")) {
				        c.add(Calendar.MONTH, - 2);
				        Date d = c.getTime();
						predicates.add(criteriaBuilder.lessThan(root.get("releaseDate").as(String.class), format.format(d)));
					}
					
				}
				//排序,都是倒序
				//按热门排序（猫眼假功能）
				if(!"".equals(movieQuery.getSortId()) && (movieQuery.getSortId() != null)) {
					if(movieQuery.getSortId().equals("2")) {
						query.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(criteriaBuilder.desc(root.get("releaseDate")));
					}
					else if(movieQuery.getSortId().equals("3")){
						query.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(criteriaBuilder.desc(root.get("avgScore")));
					}
				}
				else {
					query.where(predicates.toArray(new Predicate[predicates.size()]));
				}
                return null;
				// TODO Auto-generated method stub
//				Join join = root.join("types");
//				return criteriaBuilder.equal(join.get("id"),typeId);
			}
		},pageable);
	}
}	
