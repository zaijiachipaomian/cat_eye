package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.MovieRole;

public interface MovieRoleRepository  extends JpaRepository<MovieRole, Long>{

}
