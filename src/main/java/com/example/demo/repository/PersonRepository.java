package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Person;

public interface PersonRepository  extends JpaRepository<Person, Long>{
	
	@Query(nativeQuery = true , value = " select id , name , avator as photo from person where name like %?1% limit 3")
	public List<Map<String,Object>> searchByName(String name);
	
}
