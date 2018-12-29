package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	/**
	 * 请求方式Get
	 * 需要一个参数，参数名为typeName，表示增加的电影类型名
	 */
	//管理员增加电影类型
	@GetMapping("/admin/add")
	public Object addType(HttpServletRequest request,String typeName) {
		System.out.println("typeName="+typeName);
		HttpSession session=request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		Response re=new Response();
		if(admin!=null) {
			//判断该类型名是否存在
			if(typeRepository.findByName(typeName)==null) {
				Type type=new Type(typeName);
				if(typeRepository.save(type)!=null) {
					re.setCode(200);
					re.setData("添加成功");
					return re;
				}
				else{
					re.setCode(400);
					re.setData("添加失败");
					return re;
				}
			}
			else {
				re.setCode(400);
				re.setData("该类型已存在");
				return re;
			}	
		}	
		else {
			re.setCode(400);
			re.setData("管理员未登录");
			return re;
		}
	}
	
	/**
	 * 请求方式Get
	 * 需要一个参数，参数名为nawTypeName，表示新的电影类型名
	 */
	//管理员修改电影类型
	@GetMapping("/admin/update/{id}")
	public Object updateType(HttpServletRequest request,@PathVariable Long id,String newTypeName) {
		HttpSession session=request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		Response re=new Response();
		if(admin!=null) {
			Type type=typeRepository.findById(id).orElse(null);
			if(type!=null) {
				type.setName(newTypeName);
				if(typeRepository.save(type)!=null) {
					re.setCode(200);
					re.setData("修改成功");
					return re;
				}
				else {
					re.setCode(400);
					re.setData("修改失败");
					return re;
				}
			}
			else {
				re.setCode(400);
				re.setData("类型ID错误");
				return re;
			}
		}	
		else {
			re.setCode(400);
			re.setData("管理员未登录");
			return re;
		}
	}
	
	//管理员删除电影类型
	@GetMapping("/admin/delete/{id}")
	public Object deleteType(HttpServletRequest request,@PathVariable Long id) {
		HttpSession session=request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		Response re=new Response();
		if(admin!=null) {
			Type type=typeRepository.findById(id).orElse(null);
			if(type!=null) {
				try{
					typeRepository.delete(type);
					re.setCode(200);
					re.setData("删除成功");
					return re;
				}catch (Exception e) {
					e.printStackTrace();
					re.setCode(400);
					re.setData("删除失败");
					return re;
				}
				
			}
			else {
				re.setCode(400);
				re.setData("类型ID错误");
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
