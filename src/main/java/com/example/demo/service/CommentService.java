package com.example.demo.service;

import java.util.Map;

import com.example.demo.entity.Comment;

public interface CommentService {
	public boolean addComment(Comment comment);
	public void delComment(Long id);
	public Map<String,Object> findCommentByUserId(Long id);
	public Map<String,Object> findCommentByMovieId(Long id);
	public Map<String,Object> findByMovieOrderByDate(Long id);
}
