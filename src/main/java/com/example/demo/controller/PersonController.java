package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.service.PersonService;
import com.example.demo.util.FileUpload;

@RestController
@RequestMapping("/person")
public class PersonController {
	
	@Autowired
	PersonService personService;
	
	@Autowired
	PersonRepository personRepository;
	
	/**
	 * @author 池章立
	 * @param Person 电影人  、 file 电影人的头像
	 * @return success表示成功  false表示失败
	 * 	从表单插入或更新电影人数据完成，数据在表单的格式
	 * 	POST方式，当表单有id时表示更新，没有id时表示插入
		表单字段名		表单数据类型			表单值例子
		id(可选)			TEXT				1
		fileName		File				jh.jpg
		name			TEXT				温子仁
		gender			TEXT				男
		nation			TEXT				美国
		birthday		TEXT				2018-12-25
		introduction	TEXT				海王是一部新电影
	 */
	@PostMapping("/admin/addOrUpdate")
	public String insertOrUpdatePerson(Person person , @RequestParam("fileName") MultipartFile file){
		//文件上传成功，返回文件的路径加名字,否则返回false
		String result = FileUpload.fileUpload(file);
		if(!result.equals("false")) {
			personService.savePerson(person, result);
			return "true";
		}
		return result;
	}
	/**
	 * 根据id删除person，请求方式delete	
	 * @param id person表的数据id
	 * @return true表示删除成功，false表示删除失败
	 */
	@DeleteMapping("/admin/delete/{id}")
	public String deletePerson(@PathVariable Long id){
		try {
			personService.deletePerson(id);
		}
		catch (Exception e) {
			System.out.println("删除失败");
			e.printStackTrace();
			return "false";
		}
		return "true";
	}
	
	@GetMapping("/get/{id}")
	public Map<String, Object> getPerson(@PathVariable Long id){
		Map<String , Object> map = new HashMap<>();
		Person person = personService.getPerson(id);
		map.put("person", person);
		return map;
	}
	
	@GetMapping("/admin/get/")
	public Object getPersonList( @PageableDefault(page=0,size=10)Pageable pageable){
		//Map<String , Object> map = new HashMap<>();
		Page<Person> personPage = personRepository.findAll(pageable);
		return personPage;
	}
	
}
