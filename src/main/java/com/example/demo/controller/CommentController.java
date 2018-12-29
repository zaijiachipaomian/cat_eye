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
		Response re=new Response();
		//获取当前登录用户
		user=(User) session.getAttribute("user");
		movie=movieService.findMovieById(movie_id);
		if(user!=null) {
			if(movie!=null) {
				if(commentService.addComment(comment,user,movie)) {
					re.setCode(200);
					re.setData("ok");
					return re;
				}
				else {
					re.setCode(400);
					re.setData("添加错误");
					return re;
				}
			}
			else {
				re.setCode(400);
				re.setData("电影不存在");
				return re;
			}
			
		}
		else {
			re.setCode(400);
			re.setData("用户未登录");
			return re;
		}
	}

	//用户删除评论通过评论ID
	@ResponseBody
	@PostMapping("/delete/{id}")
	public Object deleteComment(HttpServletRequest request,@PathVariable Long id) {
		HttpSession session=request.getSession();
		User user=null;
		Comment comment=null;
		Response re=new Response();
		user=(User) session.getAttribute("user");
		comment=commentService.findCommentById(id);
		if(user!=null&&comment!=null) {
			//判断该评论是否属于该用户，若属于则评论可删除
			if(comment.getUser().getId()==user.getId()) {
				commentService.delComment(id);
				re.setCode(200);
				re.setData("ok");
				return re;
			}
			else {
				re.setCode(400);
				re.setData("用户错误");
				return re;
			}
		}
		else {
			re.setCode(400);
			re.setData("未登录或评论不存在");
			return re;
		}
		
	}
	
	//获取某部电影的评论
	@ResponseBody
	@GetMapping("/movie/{movie_id}")
	public Object getMovieComment(@PathVariable Long movie_id,@PageableDefault(page=0,size=5)Pageable pageable){
		Map<String,Object> map=new HashMap<String, Object>();
		Page<Comment> comment=null;	
		Response re=new Response();
		comment=commentService.findCommentByMovieId(movie_id,pageable);
		if(comment!=null) {
			map.put("comment",comment.getContent());
			return map;
		}
		else {
			re.setCode(400);
			re.setData("电影不存在");
			return re;
		}
		
	}
	
	//获取某个用户的评论
	@ResponseBody
	@GetMapping("/user/{user_id}")
	public Object getUserComment(HttpServletRequest request,@PathVariable Long user_id,@PageableDefault(page=0,size=5)Pageable pageable){
		Response re=new Response();
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		Page<Comment> comment=null;	
		if(user!=null) {
			if(user.getId()==user_id) {
				//TODO comment==null时
				comment=commentService.findCommentByUserId(user_id, pageable);
				if(comment!=null) {
					re.setCode(200);
					re.setData(comment);
					return re;
				}
				
				else {
					re.setCode(200);
					re.setData("该用户无评论");
					return re;
				}
				
			}
			else {
				re.setCode(400);
				re.setData("用户错误");
				return re;
			}
		}
		else {
			re.setCode(400);
			re.setData("用户未登录");
			return re;
		}
	}
	
	//管理员删除评论通过评论ID
	@ResponseBody
	@GetMapping("/admin/delete/{id}")
	public Object delComment(HttpServletRequest request,@PathVariable Long id) {
		HttpSession session=request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		Response re=new Response();
		if(admin!=null) {
			commentService.delComment(id);
			re.setCode(200);
			re.setData("删除成功");
			return re;
		}
		else {
			re.setCode(400);
			re.setData("管理员未登录");
			return re;
		}
	}
	
	//管理员查看所有评论，分页默认每页10条
	@ResponseBody
	@GetMapping("/admin/get")
	public Object getAllComment(HttpServletRequest request,@PageableDefault(page=0,size=10)Pageable pageable) {
		HttpSession session=request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		Response re=new Response();
		Page<Comment> ls=null;
		if(admin!=null) {
			ls=commentService.findAllComment(pageable);
			if(ls!=null) {
				re.setCode(200);
				re.setData(ls);
				return re;
			}
			else{
				re.setCode(400);
				re.setData("获取评论出错");
				return re;
			}
			
		}
		else {
			re.setCode(400);
			re.setData("管理员未登录");
			return re;
		}
	}
}
