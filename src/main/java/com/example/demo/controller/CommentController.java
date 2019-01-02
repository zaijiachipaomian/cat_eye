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
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;
import com.example.demo.service.MovieService;
import com.example.demo.controller.AdminHomeController.Admin;
import com.example.demo.controller.AdminHomeController.Response;
@Controller
@RequestMapping("/comment")
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	MovieService movieService;
	@Autowired
	CommentRepository commentRepository;
	
	@ResponseBody
	@PostMapping("/add/{movie_id}")
	public Object addComment(HttpServletRequest request,@PathVariable Long movie_id,Comment comment) {
		HttpSession session=request.getSession();
		User user=null;
		Movie movie=null;
		//获取当前登录用户
		user=(User) session.getAttribute("user");
		movie=movieService.findMovieById(movie_id);
		if(user!=null) {
			if(movie!=null) {
				if(commentService.addComment(comment,user,movie)) {
					return new Response(200,"增加成功");
				}
				else {
					return new Response(400,"增加错误");
				}
			}
			else {
				return new Response(400,"电影不存在");
			}
			
		}
		else {
			return new Response(400,"用户未登录");
		}
	}

	//用户删除评论通过评论ID
	@ResponseBody
	@PostMapping("/delete/{id}")
	public Object deleteComment(HttpServletRequest request,@PathVariable Long id) {
		HttpSession session=request.getSession();
		User user=null;
		Comment comment=null;
		user=(User) session.getAttribute("user");
		comment=commentService.findCommentById(id);
		if(user!=null&&comment!=null) {
			//判断该评论是否属于该用户，若属于则评论可删除
			if(comment.getUser().getId()==user.getId()) {
				try{
					commentService.delComment(id);
					return new Response(200,"删除成功");
				}catch (Exception e) {
					e.printStackTrace();
					return new Response(400,"删除成功");
				}
				
			}
			else {
				return new Response(400,"用户错误");
			}
		}
		else {
			return new Response(400,"未登录或评论不存在");
		}
		
	}
	
	//获取某部电影的评论
	@ResponseBody
	@GetMapping("/movie/{movie_id}")
	public Object getMovieComment(@PathVariable Long movie_id,@PageableDefault(page=0,size=5)Pageable pageable){
		Map<String,Object> map=new HashMap<String, Object>();
		Page<Comment> comment=null;	
		comment=commentService.findCommentByMovieId(movie_id,pageable);
		if(comment!=null) {
			map.put("comment",comment);
			return map;
		}
		else {
			return new Response(400,"电影不存在");
		}
		
	}
	
	//获取某个用户的评论
	@ResponseBody
	@GetMapping("/user/{user_id}")
	public Object getUserComment(HttpServletRequest request,@PathVariable Long user_id,@PageableDefault(page=0,size=5)Pageable pageable){
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		Page<Comment> comment=null;	
		if(user!=null) {
			if(user.getId()==user_id) {
				comment=commentService.findCommentByUserId(user_id, pageable);
				if(comment!=null) {
					return new Response(200,comment);
				}
				
				else {
					return new Response(200,"该用户无评论");
				}
				
			}
			else {
				return new Response(400,"用户错误");
			}
		}
		else {
			return new Response(400,"用户未登录");
		}
	}
	
	//管理员删除评论通过评论ID
	@ResponseBody
	@GetMapping("/admin/delete/{id}")
	public Object delComment(@PathVariable Long id) {
		try{
			commentService.delComment(id);
			return new Response(200,"删除成功");
		}catch (Exception e) {
			e.printStackTrace();
			return new Response(400,"删除失败");
		}
	}
	
	//管理员查看所有评论，分页默认每页10条
	@ResponseBody
	@GetMapping("/admin/get")
	public Object getAllComment(@PageableDefault(page=0,size=10)Pageable pageable) {	
		Page<Comment> ls=null;
		ls=commentService.findAllComment(pageable);
		if(ls!=null) {
			return new Response(200,ls);
		}
		else{
			return new Response(400,"获取评论出错");
		}
	}
}
