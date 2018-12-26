package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Type;

public interface TypeRepository extends JpaRepository<Type, Long>{
	
	/**
	 * 多对多查询，从电影到类型，需要按顺序调用这两个方法
	 * @param id
	 * @return
	 */
	@Query(nativeQuery = true , value ="select type_id from movie_types where movie_id = ?1")
	Long[] findTypeIdByMovieId(Long id);
	
	@Query(value =" select t from Type t where t.id in ?1 ")
	List<Type> findTypeByTypeId(Long[] ids);

}
