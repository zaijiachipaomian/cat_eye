package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class MovieRole {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Movie movie;
	//角色 -> 导演 、 剪辑等等
	private String role;
	
	//可能多对一
	private Long personId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Movie getMovie() {
		return movie;
	}

	@JsonBackReference
	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public MovieRole(String role, Long personId) {
		super();
		this.role = role;
		this.personId = personId;
	}

	public MovieRole() {
		super();
	}

	@Override
	public String toString() {
		return "MovieRole [id=" + id + ", role=" + role + ", personId=" + personId + "]";
	}
	
	
	
	
	
}
