package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

//电影今日票房类 https://maoyan.com/里面的今日票房
public class TodayBoxOffice {
	
	private String movieUrl;
	private String movieName;
	private String boxOffice;
	private Movie movie;
	private List<Photo> photos = new ArrayList<Photo>();
	public String getMovieUrl() {
		return movieUrl;
	}
	public void setMovieUrl(String movieUrl) {
		this.movieUrl = movieUrl;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	
	public String getBoxOffice() {
		return boxOffice;
	}
	public void setBoxOffice(String boxOffice) {
		this.boxOffice = boxOffice;
	}
	public TodayBoxOffice(String movieUrl, String movieName, String boxOffice) {
		super();
		this.movieUrl = movieUrl;
		this.movieName = movieName;
		this.boxOffice = boxOffice;
	}
	public TodayBoxOffice() {
		super();
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	public List<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
	@Override
	public String toString() {
		return "TodayBoxOffice [movieUrl=" + movieUrl + ", movieName=" + movieName + ", boxOffice=" + boxOffice + "]";
	}
}
