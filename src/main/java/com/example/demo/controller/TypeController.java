package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.AdminHomeController.Admin;
import com.example.demo.controller.AdminHomeController.Response;
import com.example.demo.entity.Type;
import com.example.demo.repository.TypeRepository;

@RestController
@RequestMapping("/type")
public class TypeController {
	
	@Autowired
	TypeRepository typeRepository;
	
	//获取所有电影类型
	@GetMapping("/list")
	public List<Type> getTypeList(){
		return typeRepository.findAll();
	}
	//获取所有电影类型 分页
	@GetMapping("/list/pageable")
	public Object getTypeListPage(@PageableDefault(page=0,size=10) Pageable pageable){
		return new Response(200,typeRepository.findAll(pageable));
	}
	/**
	 * 请求方式Get
	 * 需要一个参数，参数名为typeName，表示增加的电影类型名
	 */
	//管理员增加电影类型
	@GetMapping("/admin/addOrUpdate")
	public Object addType(Type type) {
		if(type!=null) {
			if(typeRepository.save(type)!=null) {
				return new Response(200,"成功");
			}
			else {
				return new Response(400,"失败");
			}
		}
		else {
			return new Response(400,"类型ID错误");
		}
	}
	
	/**
	 * 请求方式Get
	 * 需要一个参数，参数名为nawTypeName，表示新的电影类型名
	 */
	//管理员修改电影类型
//	@GetMapping("/admin/update/{id}")
//	public Object updateType(@PathVariable Long id,String newTypeName) {
//		Type type=typeRepository.findById(id).orElse(null);
//		if(type!=null) {
//			type.setName(newTypeName);
//			if(typeRepository.save(type)!=null) {
//				return new Response(200,"修改成功");
//			}
//			else {
//				return new Response(400,"修改失败");
//			}
//		}
//		else {
//			return new Response(400,"类型ID错误");
//		}
//	}
	
	//管理员删除电影类型
	@GetMapping("/admin/delete/{id}")
	public Object deleteType(@PathVariable Long id) {
		Type type=typeRepository.findById(id).orElse(null);
		if(type!=null) {
			try{
				typeRepository.delete(type);
				return new Response(200,"删除成功");
			}catch (Exception e) {
				e.printStackTrace();
				return new Response(400,"删除失败");
			}
			
		}
		else {
			return new Response(400,"类型ID错误");
		}
	}
	
	
}
