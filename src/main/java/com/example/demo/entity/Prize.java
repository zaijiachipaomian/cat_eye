package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Prize {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String logoUrl;
	private String detail;
	@ManyToOne
	private Movie movie;
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
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	public Prize(String name, String logoUrl, String detail) {
		super();
		this.name = name;
		this.logoUrl = logoUrl;
		this.detail = detail;
	}
	public Prize() {
		super();
	}
	@Override
	public String toString() {
		return "Prize [id=" + id + ", name=" + name + ", logoUrl=" + logoUrl + ", detail=" + detail + "]";
	}
	
	
	
	
}
