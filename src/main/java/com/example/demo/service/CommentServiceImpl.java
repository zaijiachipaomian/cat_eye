package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	CommentRepository commentRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MovieRepository movieRepository;
	@Override
	public boolean addComment(Comment comment,User user,Movie movie) {
		// TODO Auto-generated method stub
		boolean isFlag=false;
		if(user!=null&&movie!=null) {
			user.addComment(comment);
			movie.addComment(comment);
			commentRepository.save(comment);
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
	public Page<Comment> findCommentByUserId(Long id,Pageable pageable) {
		// TODO Auto-generated method stub
		Page<Comment> comment=null;
		User user=null;
		user=userRepository.findById(id).get();
		if(user!=null) {
			comment=commentRepository.findByUser(user, pageable);
		}
		return comment;
	}

	@Override
	public Page<Comment> findCommentByMovieId(Long id,Pageable pageable) {
		// TODO Auto-generated method stub
		Page<Comment> comment=null;
		Movie movie=null;
		List<Comment> ls=new ArrayList<Comment>();
		movie=movieRepository.findById(id).get();
		if(movie!=null) {
			comment= commentRepository.findByMovie(movie,pageable);
			
		}
		return comment;
	}

	@Override
	public Page<Comment> findByMovieOrderByDate(Long id,Pageable pageable) {
		// TODO Auto-generated method stub
		Page<Comment> comment=null;
		Movie movie=null;
		movie=movieRepository.findById(id).get();
		if(movie!=null) {
			comment=commentRepository.findByMovieOrderByDate(movie,pageable);
			
		}
		return comment;
	}

}
