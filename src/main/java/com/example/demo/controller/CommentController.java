package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;
import com.example.demo.entity.User;
import com.example.demo.service.CommentService;
import com.example.demo.service.MovieService;

@Controller
@RequestMapping("/comment")
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	MovieService movieService;
	
	@ResponseBody
	@PostMapping("/add/{movie_id}")
	public String addComment(@PathVariable Long movie_id,HttpServletRequest request,Comment comment) {
		HttpSession session=request.getSession();
		User user=null;
		Movie movie=null;
		String result="false";
		user=(User) session.getAttribute("user");
		movie=movieService.findById(movie_id);
		if(commentService.addComment(comment,user,movie)) {
			result="true";
		}
		return result;
	}
	
	@ResponseBody
	@PostMapping("/delete/{id}")
	public String deleteComment(@PathVariable Long id) {
		commentService.delComment(id);
		return "true";
	}
	
	@ResponseBody
	@GetMapping("/movie/{movie_id}")
	public Map<String,Object> getMovieComment(@PathVariable Long movie_id,@PageableDefault(page=0,size=10)Pageable pageable){
		Map<String,Object> map=new HashMap<String, Object>();
		Page<Comment> comment=null;	
		comment=commentService.findCommentByMovieId(movie_id,pageable);
		map.put("comment",comment.getContent());
		return map;
	}
	
	@ResponseBody
	@GetMapping("/user/{user_id}")
	public Map<String,Object> getUserComment(@PathVariable Long user_id,@PageableDefault(page=0,size=10)Pageable pageable){
		Map<String,Object> map=new HashMap<String, Object>();
		Page<Comment> comment=null;	
		comment=commentService.findCommentByUserId(user_id, pageable);
		map.put("comment", comment.getContent());
		return map;
	}
}
