package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 50)
	private String name;
	private String url;
	private String pic;
	@ManyToOne
	private Movie movie;
	public void clearMovie() {
		this.movie.getNewsList().remove(this);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
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
		return "News [id=" + id + ", name=" + name + ", url=" + url + ", pic=" + pic + "]";
	}
	public News(String name, String url, String pic) {
		super();
		this.name = name;
		this.url = url;
		this.pic = pic;
	}
	public News() {
		super();
	}
	
	
	
	
}
