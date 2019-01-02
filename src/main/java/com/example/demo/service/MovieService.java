package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import com.example.demo.entity.Movie;
import com.example.demo.entity.MovieQuery;

public interface MovieService {
	boolean addMovie(Movie movie, List<String> photoUrl , String posterUrl);
	public void delMovie(Long id);
	public Movie findMovieById(Long id);
	public boolean updateMovie(Movie movie);
	//public Map<String,Object> findMovieByStatus(String status);
	public Map<String,Object> findByTypesAndAreaAndReleaseDate(String type,String area,String releaseDate);
	public Map<String,Object> findByTypesAndAreaAndReleaseDateOrderByReleaseDate(String type,String area,String releaseDate);
	public Set<String> findDistinctAreaSet();
	public Set<String> findDistinctReleaseSet();
	public Page<Movie> getMovieList(MovieQuery movieQuery , Pageable pageable);
	//主页中获得正在热映的且今日票房最高的8部电影，并按票房排序.
	public List<Map<String,Object>> getReleasingMovieOrderByBoxOfficeLimit8() throws Exception;
	//显示正在热映电影的总数量。
	Long getReleasingCount();
	

}
