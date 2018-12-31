package com.example.demo.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.Movie;
import com.example.demo.repository.MovieRepository;
import com.example.demo.util.MaoYanBoxOfficeApi;

@Service
public class BoardServiceImpl implements BoardSerivce{

	@Autowired
	MovieRepository movieRepository;
	
	@Override
	public Page<Movie> getReleasingBoard(Pageable pageable) {
		return movieRepository.findAll(new Specification<Movie>() {
			@Override
			public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				//正在热映（过去两个月）
				Calendar c = Calendar.getInstance();
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		        c.add(Calendar.MONTH, - 2);
		        Date d = c.getTime();
		        predicates.add(criteriaBuilder.greaterThan(root.get("releaseDate").as(String.class), format.format(d)));
				predicates.add(criteriaBuilder.lessThan(root.get("releaseDate").as(String.class), format.format(date)));
			
				//按评价排序
				query.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(criteriaBuilder.desc(root.get("avgScore")));
                return null;
			}
		},pageable);
	}

	@Override
	public List<Map<String , Object>> getBoxOfficeBoard(Pageable pageable) throws Exception {
		// TODO Auto-generated method stub
		List<Map<String , Object>> list = new ArrayList<>();
		Response resp = MaoYanBoxOfficeApi.getMaoYanBoxOfficeApi();
		JSONArray jsonArray = MaoYanBoxOfficeApi.parseMaoYanBoxOfficeResp(resp);
		System.out.println(jsonArray);
		//JSONObject jsonObject2 = jsonArray.getJSONObject(0);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tempObject = jsonArray.getJSONObject(i);
			Long movieId = Long.parseLong(tempObject.get("movieId").toString());
			String sumBoxInfo = tempObject.get("sumBoxInfo").toString();
			String boxInfo = tempObject.get("boxInfo").toString();
			String releaseInfo = tempObject.get("releaseInfo").toString();
			if(movieRepository.existsById(movieId)) {
				Map<String , Object> map = new HashMap<>();
				Movie movie = movieRepository.findById(movieId).get();
				map.put("movie", movie);
				map.put("boxInfo", boxInfo);
				map.put("sumBoxInfo", sumBoxInfo);
				map.put("releaseInfo", releaseInfo);
				list.add(map);
				System.out.println(list);
			}
		}
		return list;
	}

	@Override
	public Page<Movie> getTop100Board(Pageable pageable) {
		return movieRepository.findAll(new Specification<Movie>() {
			@Override
			public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				//历史总榜，没有时间限制
				//按评价和评价人数排序
				query.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(criteriaBuilder.desc(root.get("avgScore")),criteriaBuilder.desc(root.get("commentCount")));
                return null;
			}
		},pageable);
	}

}
