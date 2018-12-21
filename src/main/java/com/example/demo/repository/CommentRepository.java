package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	//查看某部电影所有评论
	public List<Comment> findByMovie(Movie movie);
	//通过评论时间排序
	public List<Comment> findByMovieOrderByDate(Movie movie);
	
}
