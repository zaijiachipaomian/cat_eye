package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Photo {
	@Id
	@GeneratedValue
	private Long id;
	private String url;
	@ManyToOne
	private Movie movie;
	public void clearMovie() {
		this.movie.getPhotos().remove(this);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Movie getMovie() {
		return movie;
	}
	@JsonBackReference
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	@Override
	public String toString() {
		return "Photo [id=" + id + ", url=" + url + ", movie=" + movie + "]";
	}
	public Photo(String url) {
		super();
		this.url = url;
	}
	public Photo() {
		super();
	}
	
	
	
}
