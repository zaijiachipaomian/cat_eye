package com.example.demo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Date date;

	private Float score;
	
	private String content;

	@ManyToOne
	private Movie movie;
	public void clearComment() {
		this.movie.getComments().remove(this);
	}
	@ManyToOne
	private User user;
	
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	
	

	public Comment(Date date, Float score, String content) {
		super();
		this.date = date;
		this.score = score;
		this.content = content;
	}

	public Comment() {
		super();
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", date=" + date + ", score=" + score + ", content=" + content + "]";
	}
	
	
	
}
