package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.News;

public interface NewsRepository extends JpaRepository<News, Long>{

}
