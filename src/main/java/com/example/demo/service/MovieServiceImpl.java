package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Movie;
import com.example.demo.entity.Photo;
import com.example.demo.repository.MovieRepository;
@Service
public class MovieServiceImpl implements MovieService {
	
	@Autowired
	MovieRepository movieRepository;
	@Override
	public boolean addMovie(Movie movie , List<String> photoUrlList,String posterUrl) {
		// TODO Auto-generated method stub
		boolean isFlag=false;
		Movie m=null;
		//List<Photo> photoList = new ArrayList<>();
		for(String photoUrl : photoUrlList) {
			movie.addPhoto(new Photo(photoUrl));
		}
		movie.setPoster(posterUrl);
		System.out.println(movie);
		System.out.println(movie.getPhotos());
		m=movieRepository.save(movie);
		System.out.println(m);
		if(m!=null) {
			isFlag=true;
		}
		return isFlag;
	}

	@Override
	public void delMovie(Long id) {
		// TODO Auto-generated method stub
		movieRepository.deleteById(id);
	}

	@Override
	public boolean updateMovie(Movie movie) {
		// TODO Auto-generated method stub
		boolean isFlag=false;
		Movie m=null;
		m=movieRepository.save(movie);
		if(m!=null) {
			isFlag=true;
		}
		return false;
	}

	@Override
	public Map<String, Object> findMovieByStatus(String status) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		List<Movie> ls=new ArrayList<Movie>();
		ls=movieRepository.findByStatus(status);
		if(ls.size()>0) {
			map.put("movie", ls);
		}
		return map;
	}

	@Override
	public Map<String, Object> findByTypesAndAreaAndReleaseDate(String types, String area, String release_date) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		List<Movie> ls=new ArrayList<Movie>();
		ls=movieRepository.findByTypesAndAreaAndReleaseDate(types, area, release_date);
		if(ls.size()>0) {
			map.put("movie", ls);
		}
		return map;
	}

	@Override
	public Map<String, Object> findByTypesAndAreaAndReleaseDateOrderByReleaseDate(String types, String area, String release_date) {
		Map<String,Object> map=new HashMap<String,Object>();
		List<Movie> ls=new ArrayList<Movie>();
		ls=movieRepository.findByTypesAndAreaAndReleaseDateOrderByReleaseDate(types, area, release_date);
		if(ls.size()>0) {
			map.put("movie", ls);
		}
		return map;
	}

	@Override
	public Movie findMovieById(Long id) {
		// TODO Auto-generated method stub
		return movieRepository.findById(id).get();
	}

}