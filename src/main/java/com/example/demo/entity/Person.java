package com.example.demo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Person {
	@Id
	@GeneratedValue
	private Long id;
	private String avator;
	private String name;
	private String gender;
	private String nation;
	private Date birthday;
	private String introduction;
    @ManyToMany(mappedBy = "persons",fetch = FetchType.EAGER)
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
	public String getAvator() {
		return avator;
	}
	public void setAvator(String avator) {
		this.avator = avator;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public List<Movie> getMovies() {
		return movies;
	}
	
	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}
	public Person(String avator, String name, String gender, String nation, Date birthday, String introduction) {
		super();
		this.avator = avator;
		this.name = name;
		this.gender = gender;
		this.nation = nation;
		this.birthday = birthday;
		this.introduction = introduction;
	}
	public Person() {
		super();
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", avator=" + avator + ", name=" + name + ", gender=" + gender + ", nation="
				+ nation + ", birthday=" + birthday + ", introduction=" + introduction + "]";
	}
    
}
