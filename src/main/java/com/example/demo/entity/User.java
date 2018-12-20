package com.example.demo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class User {
	@Id
	@GeneratedValue
	private Long Id;
	
	private String avator;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String phone;
	
	private Date regDate;
	
	private String statu;
	
	@OneToMany(mappedBy = "user" ,cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REMOVE,			
	},fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	private List<Comment> comments = new ArrayList<>();
	
	public void addComment(Comment comment) {
		comment.setUser(this);
		comments.add(comment);
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getAvator() {
		return avator;
	}

	public void setAvator(String avator) {
		this.avator = avator;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getStatu() {
		return statu;
	}

	public void setStatu(String statu) {
		this.statu = statu;
	}

	public List<Comment> getComments() {
		return comments;
	}

	@JsonBackReference
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	
}
