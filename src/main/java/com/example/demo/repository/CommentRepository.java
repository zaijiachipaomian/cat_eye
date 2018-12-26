package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;
import com.example.demo.entity.User;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	//查看某部电影所有评论
	public Page<Comment> findByMovie(Movie movie,Pageable pageable);
	//通过评论时间排序
	public Page<Comment> findByMovieOrderByDate(Movie movie,Pageable pageable);
	//通过用户ID查找评论
	public Page<Comment> findByUser(User user,Pageable pageable);
	
}
