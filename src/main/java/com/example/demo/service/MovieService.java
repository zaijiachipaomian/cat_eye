package com.example.demo.service;

import java.util.Map;

import com.example.demo.entity.Movie;

public interface MovieService {
	public boolean addMovie(Movie movie);
	public void delMovie(Long id);
	public boolean updateMovie(Movie movie);
	public Map<String,Object> findMovieByStatus(String status);
	public Map<String,Object> findByTypesAndAreaAndReleaseDate(String type,String area,String releaseDate);
	public Map<String,Object> findByTypesAndAreaAndReleaseDateOrderByReleaseDate(String type,String area,String releaseDate);
}
