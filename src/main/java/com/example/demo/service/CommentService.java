package com.example.demo.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;
import com.example.demo.entity.User;

public interface CommentService {
	public boolean addComment(Comment comment,User user,Movie movie);
	public void delComment(Long id);
	public Comment findCommentById(Long id);
	public Page<Comment> findAllComment(Pageable pageable);
	public Page<Comment> findCommentByUserId(Long id,Pageable pageable);
	public Page<Comment> findCommentByMovieId(Long id,Pageable pageable);
	public Page<Comment> findByMovieOrderByDate(Long id,Pageable pageable);
}
