package com.example.demo.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;

@Service
public class PersonServiceImpl implements PersonService {
	
	@Autowired
	PersonRepository personRepository;
	
	@Override
	public Person savePerson(Person person, String avatorUrl) {
		// TODO Auto-generated method stub
		person.setAvator(avatorUrl);
		return personRepository.save(person);
	}

	@Override
	public void deletePerson(Long id) {
		// TODO Auto-generated method stub
		personRepository.deleteById(id);
	}

	@Override
	public Person getPerson(Long id) {
		// TODO Auto-generated method stub
		Person person = personRepository.findById(id).get();
		return person;
	}

	@Override
	public Object searchByName(String name) {
		// TODO Auto-generated method stub
		return personRepository.searchByName(name);
	}
}
