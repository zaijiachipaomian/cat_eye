package com.example.demo.service;


import com.example.demo.entity.Person;

public interface PersonService {
	public Person savePerson(Person person , String avatorUrl);
	
	public void deletePerson(Long id);
	
	public Person getPerson(Long id);
	
	public Object searchByName(String name);
}
