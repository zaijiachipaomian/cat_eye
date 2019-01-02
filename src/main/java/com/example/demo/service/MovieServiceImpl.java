package com.example.demo.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
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

import com.example.demo.entity.Movie;
import com.example.demo.entity.MovieQuery;
import com.example.demo.entity.Photo;
import com.example.demo.repository.MovieRepository;
import com.example.demo.util.MaoYanBoxOfficeApi;
@Service
public class MovieServiceImpl implements MovieService {
	
	@Autowired
	MovieRepository movieRepository;
	@Override
	public boolean addMovie(Movie movie , List<String> photoUrlList,String posterUrl) {
		// TODO Auto-generated method stub
		boolean isFlag=false;
		Movie m=null;
		//List<Photo> photoList = new ArrayList<>();
		if(photoUrlList != null)
			for(String photoUrl : photoUrlList) {
				movie.addPhoto(new Photo(photoUrl));
			}
		if(posterUrl != null)
			movie.setPoster(posterUrl);
		System.out.println(movie);
		System.out.println(movie.getPhotos());
		m=movieRepository.save(movie);
		System.out.println(m);
		if(m!=null) {
			isFlag=true;
		}
		return isFlag;
	}

	@Override
	public void delMovie(Long id) {
		// TODO Auto-generated method stub
		movieRepository.deleteById(id);
	}

	@Override
	public boolean updateMovie(Movie movie) {
		// TODO Auto-generated method stub
		boolean isFlag=false;
		Movie m=null;
		m=movieRepository.save(movie);
		if(m!=null) {
			isFlag=true;
		}
		return false;
	}

//	@Override
//	public Map<String, Object> findMovieByStatus(String status) {
//		// TODO Auto-generated method stub
//		Map<String,Object> map=new HashMap<String,Object>();
//		List<Movie> ls=new ArrayList<Movie>();
//		ls=movieRepository.findByStatus(status);
//		if(ls.size()>0) {
//			map.put("movie", ls);
//		}
//		return map;
//	}

	@Override
	public Map<String, Object> findByTypesAndAreaAndReleaseDate(String types, String area, String release_date) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		List<Movie> ls=new ArrayList<Movie>();
		ls=movieRepository.findByTypesAndAreaAndReleaseDate(types, area, release_date);
		if(ls.size()>0) {
			map.put("movie", ls);
		}
		return map;
	}

	@Override
	public Map<String, Object> findByTypesAndAreaAndReleaseDateOrderByReleaseDate(String types, String area, String release_date) {
		Map<String,Object> map=new HashMap<String,Object>();
		List<Movie> ls=new ArrayList<Movie>();
		ls=movieRepository.findByTypesAndAreaAndReleaseDateOrderByReleaseDate(types, area, release_date);
		if(ls.size()>0) {
			map.put("movie", ls);
		}
		return map;
	}

	@Override
	public Movie findMovieById(Long id) {
		// TODO Auto-generated method stub
		return movieRepository.findById(id).get();
	}

	@Override
	public Set<String> findDistinctAreaSet() {
		// TODO Auto-generated method stub
		Set<String> areaSet = movieRepository.findDistinctAreaSet();
		if(areaSet.contains(null))
			areaSet.remove(null);
		Set<String> dotSet = areaSet.stream().filter(s -> s.contains(",")).collect(Collectors.toSet());
		for(String s : dotSet) {
			String[] split = s.split(",");
			for(String tempStr : split) {
				areaSet.add(tempStr);
			}
			areaSet.remove(s);
		}
		return areaSet;
	}

	@Override
	public Set<String> findDistinctReleaseSet() {
		// TODO Auto-generated method stub
		Set<String> releaseDateSet = movieRepository.findDistinctReleaseSet();
		if(releaseDateSet.contains(null))
			releaseDateSet.remove(null);
		return releaseDateSet;
	}

	@Override
	public Page<Movie> getMovieList(MovieQuery movieQuery, Pageable pageable) {
		// TODO Auto-generated method stub
		return movieRepository.findAll(new Specification<Movie>() {
			@Override
			public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				//标签筛选
				if(!"".equals(movieQuery.getTypeId()) && (movieQuery.getTypeId() != null)) {
					Join join = root.join("types");
					predicates.add(
							criteriaBuilder.equal(join.get("id"),movieQuery.getTypeId())
							);
				}
				if(!"".equals(movieQuery.getName()) && (movieQuery.getName() != null)) {
					predicates.add(criteriaBuilder.like(root.<String>get("name"), "%"+movieQuery.getName()+"%"));
				}
				if(!"".equals(movieQuery.getArea()) && (movieQuery.getArea() != null)) {
					predicates.add(criteriaBuilder.like(root.<String>get("area"), "%"+movieQuery.getArea()+"%"));
				}
				if(!"".equals(movieQuery.getReleaseDate()) && (movieQuery.getReleaseDate() != null)) {
					Integer year = Integer.parseInt(movieQuery.getReleaseDate());
					predicates.add(criteriaBuilder.greaterThan(root.get("releaseDate").as(String.class), year + "-01-01"));
					predicates.add(criteriaBuilder.lessThan(root.get("releaseDate").as(String.class), (year + 1 + "")));
				}
				//导航栏筛选
				if(!"".equals(movieQuery.getShowType()) && (movieQuery.getShowType() != null)) {
					//Integer showType = Integer.parseInt(movieQuery.getShowType());
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Calendar c = Calendar.getInstance();
					Date date = new Date();
					c.setTime(date);
					//正在热映（过去两个月）
					if(movieQuery.getShowType().equals("1")) {
				        c.add(Calendar.MONTH, - 2);
				    
				        Date d = c.getTime();
				        
				        System.out.println(format.format(d));
				        System.out.println(format.format(date));
				        System.out.println("正在热映");
				        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("releaseDate").as(String.class), format.format(d)));
						predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("releaseDate").as(String.class), format.format(date)));
					}
					//即将上映
					if(movieQuery.getShowType().equals("2")) {
						c.add(Calendar.DATE, + 1);
						Date d = c.getTime();
						predicates.add(criteriaBuilder.greaterThan(root.get("releaseDate").as(String.class), format.format(d)));
					}
					//经典电影（上映过了两个月）
					if(movieQuery.getShowType().equals("3")) {
				        c.add(Calendar.MONTH, - 2);
				        Date d = c.getTime();
						predicates.add(criteriaBuilder.lessThan(root.get("releaseDate").as(String.class), format.format(d)));
					}
					
				}
				//排序,时间是正序排序，而评价是倒序排序	
				//按热门排序（猫眼假功能）
				if(!"".equals(movieQuery.getSortId()) && (movieQuery.getSortId() != null)) {
					if(movieQuery.getSortId().equals("2")) {
						query.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(criteriaBuilder.asc(root.get("releaseDate")));
					}
					else if(movieQuery.getSortId().equals("3")){
						query.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(criteriaBuilder.desc(root.get("avgScore")));
					}
				}
				else {
					query.where(predicates.toArray(new Predicate[predicates.size()]));
				}
                return null;
				// TODO Auto-generated method stub
//				Join join = root.join("types");
//				return criteriaBuilder.equal(join.get("id"),typeId);
			}
		},pageable);
	}

	
	//主页中获得正在热映的且今日票房最高的8部电影，并按票房排序
	@Override
	public List<Map<String, Object>> getReleasingMovieOrderByBoxOfficeLimit8() throws Exception {
		// TODO Auto-generated method stub
		List<Map<String , Object>> list = new ArrayList<>();
		//请求猫眼
		Response resp = MaoYanBoxOfficeApi.getMaoYanBoxOfficeApi();
		//解析
		JSONArray jsonArray = MaoYanBoxOfficeApi.parseMaoYanBoxOfficeResp(resp);
		//具体业务
		int count = 0;
		for (int i = 0; i < jsonArray.length() && count < 8; i++) {
			JSONObject tempObject = jsonArray.getJSONObject(i);
			Long movieId = Long.parseLong(tempObject.get("movieId").toString());
			String sumBoxInfo = tempObject.get("sumBoxInfo").toString();
			String boxInfo = tempObject.get("boxInfo").toString();
			String releaseInfo = tempObject.get("releaseInfo").toString();
			if(movieRepository.existsById(movieId)) {	
				count++;
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

	//显示正在热映电影的总数量。
	//public 
	@Override
	public Long getReleasingCount() {
		return movieRepository.count(new Specification<Movie>() {
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
				query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
			}
		});
	}

	@Override
	public Object searchByName(String name) {
		// TODO Auto-generated method stub
		return movieRepository.searchByName(name);
	}
	
}
