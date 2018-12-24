package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;

public class CommentServiceImpl implements CommentService {

	@Autowired
	CommentRepository commentRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MovieRepository movieRepository;
	@Override
	public boolean addComment(Comment comment) {
		// TODO Auto-generated method stub
		boolean isFlag=false;
		Comment c=null;
		c=commentRepository.save(comment);
		if(c!=null) {
			isFlag=true;
		}
		return isFlag;
	}

	@Override
	public void delComment(Long id) {
		// TODO Auto-generated method stub
		commentRepository.deleteById(id);
	}

	@Override
	public Map<String,Object> findCommentByUserId(Long id) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		User user=null;
		List<Comment> ls=new ArrayList<Comment>();
		user=userRepository.findById(id).get();
		if(user!=null) {
			ls=commentRepository.findByUser(user);
			if(ls.size()>0) {
				map.put("comment",ls);
			}
		}
		return map;
	}

	@Override
	public Map<String,Object> findCommentByMovieId(Long id) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		Movie movie=null;
		List<Comment> ls=new ArrayList<Comment>();
		movie=movieRepository.findById(id).get();
		if(movie!=null) {
			ls=commentRepository.findByMovie(movie);
			if(ls.size()>0) {
				map.put("comment",ls);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> findByMovieOrderByDate(Long id) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		Movie movie=null;
		List<Comment> ls=new ArrayList<Comment>();
		movie=movieRepository.findById(id).get();
		if(movie!=null) {
			ls=commentRepository.findByMovieOrderByDate(movie);
			if(ls.size()>0) {
				map.put("comment", ls);
			}
		}
		return null;
	}

}
