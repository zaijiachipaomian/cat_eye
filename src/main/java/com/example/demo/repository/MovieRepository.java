package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Movie;
import com.example.demo.entity.Type;

public interface MovieRepository extends JpaRepository<Movie, Long>{
	//通过类型和区域和上映时间查询电影
	public List<Movie> findByTypesAndAreaAndReleaseDate(String types,String area,String relase_date);
	//通过类型和区域和上映时间查询电影并通过上映时间排序
	public List<Movie> findByTypesAndAreaAndReleaseDateOrderByReleaseDate(String types,String area,String relase_date);
	//通过影片状态查询电影
	public List<Movie> findByStatus(String status);
	
	//@Query(nativeQuery = true , value = "select * from type where id = ? ")
	//@Query(" select t from Type t where t.id = ?1")
	@Query(nativeQuery = true , value =" select type_id from movie_types where movie_id = ?1  ")
	Object[] findTypeByMovieId(Long id);
	//List<Type> findTypesByMovieId(Long id);
	
}
