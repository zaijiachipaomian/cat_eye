package com.example.demo.repository;


import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Movie;
import com.example.demo.entity.Type;

public interface MovieRepository extends JpaRepository<Movie, Long>,JpaSpecificationExecutor<Movie>{
	//通过类型和区域和上映时间查询电影
	public List<Movie> findByTypesAndAreaAndReleaseDate(String types,String area,String relase_date);
	//通过类型和区域和上映时间查询电影并通过上映时间排序
	public List<Movie> findByTypesAndAreaAndReleaseDateOrderByReleaseDate(String types,String area,String relase_date);
	//通过影片状态查询电影
	public List<Movie> findByStatus(String status);
	//通过名字和分页来查询电影
	public Page<Movie> findByNameContains(String name, Pageable pageable);
	
	public Page<Movie> findByTypesId(Long typeId , Pageable pageable);
	
	@Query(nativeQuery = true , value =" select distinct area from movie ")
	public Set<String> findDistinctAreaSet();
	
//	@Query(nativeQuery = true , value =" select distinct release_date from movie ")
	@Query(value =" select function('date_format' , m.releaseDate ,'%Y') as year from Movie m group by function ('date_format' ,m.releaseDate ,'%Y') order by year desc ")
	public Set<String> findDistinctReleaseSet();
	
	/**
	 * 多对多查询，从类型到电影，需要按顺序调用这两个方法
	 * @param id
	 * @return
	 *///弃用
//	@Query(nativeQuery = true , value ="select movie_id from movie_types where type_id = ?1")
//	Long[] findMovidIdByTypeId(Long id);
//	
//	@Query(value =" select m from Movie m where m.id in ?1 ")
//	List<Movie> findMovieByMovieId(Long[] ids);
	
	
	
}
