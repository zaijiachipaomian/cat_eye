package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.controller.AdminHomeController.Response;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Prize;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.PrizeRepository;
import com.example.demo.util.FileUpload;


@Controller
@RequestMapping("/admin/prize")
public class PrizeController {
	
	@Autowired
	MovieRepository movieRepository;

	@Autowired
	PrizeRepository prizeRepository;
	
	/**
	 * 插入或更新奖项
	 * 表单字段			类型			值（例子）
	 * movieId			Text		1
	 * name				Text		奖项名字
	 * detail			Text		奖项详情
	 * fileName			FILE		奖项LOGO图
	 * id				Text		可选（当ID有值时可以更新）
	 * @param movieId 插入奖项的电影ID
	 * @param prize	插入的奖项
	 * @return	Response
	 */
	@ResponseBody
	@PostMapping("/add")
	public Object insertOrUpdatePrize(Long movieId , 
			 Prize prize,
			 @RequestParam(name = "fileName", required = false)  MultipartFile file) {
		System.out.println(movieId);
		System.out.println(prize.toString());
		try {
			String logoUrl = FileUpload.fileUpload(file);
			prize.setLogoUrl(logoUrl);
			Movie movie = movieRepository.findById(movieId).get();
			movie.addPrize(prize);
			movieRepository.save(movie);
			return new Response(200 , "插入或更新电影奖项成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400 , "插入或更新电影奖项失败");
		}
	}
	
	@ResponseBody
	@DeleteMapping("/delete/{id}")
	public Object deletePrize(@PathVariable Long id) {
		try {
			prizeRepository.deleteById(id);
			return new Response(200 , "删除电影奖项成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response(400 , "删除电影奖项失败");
		}
	}

}
