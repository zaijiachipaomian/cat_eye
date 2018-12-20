package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Type {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
    
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Movie> movies = new ArrayList<>();
    
    public void addMovie(Movie movie) {
    	movies.add(movie);
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

	public List<Movie> getMovies() {
		return movies;
	}
	@JsonBackReference
	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}

	@Override
	public String toString() {
		return "Type [id=" + id + ", name=" + name + ", movies=" + movies + "]";
	}
    
    
}
