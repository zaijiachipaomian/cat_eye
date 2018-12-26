package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.entity.Movie;

public interface MovieService {
	boolean addMovie(Movie movie, List<String> photoUrl , String posterUrl);
	public void delMovie(Long id);
	public Movie findMovieById(Long id);
	public boolean updateMovie(Movie movie);
	public Map<String,Object> findMovieByStatus(String status);
	public Map<String,Object> findByTypesAndAreaAndReleaseDate(String type,String area,String releaseDate);
	public Map<String,Object> findByTypesAndAreaAndReleaseDateOrderByReleaseDate(String type,String area,String releaseDate);
}
