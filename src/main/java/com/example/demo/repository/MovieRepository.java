package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>{
	//通过类型和区域和上映时间查询电影
	public List<Movie> findByTypesAndAreaAndReleaseDate(String types,String area,String relase_date);
	//通过类型和区域和上映时间查询电影并通过上映时间排序
	public List<Movie> findByTypesAndAreaAndReleaseDateOrderByReleaseDate(String types,String area,String relase_date);
}
