package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.MovieRole;
import com.example.demo.entity.Type;

public interface MovieRoleRepository  extends JpaRepository<MovieRole, Long>{
	

}
