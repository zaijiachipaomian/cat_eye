package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Movie;

public interface BoardSerivce {
	
	public Page<Movie> getReleasingBoard(Pageable pageable);

	public List<Map<String, Object>> getBoxOfficeBoard(Pageable pageable) throws IOException, Exception;
	
	public Page<Movie> getTop100Board(Pageable pageable);
}
