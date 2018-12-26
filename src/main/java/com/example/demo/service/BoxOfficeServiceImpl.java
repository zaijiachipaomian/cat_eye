package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jsoup.Connection.Response;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Photo;
import com.example.demo.entity.TodayBoxOffice;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.PhotoRepository;

//API中重要的字段
//System.out.println(obj.get("movieId"));
//System.out.println(obj.get("sumBoxInfo"));
//System.out.println(obj.get("boxInfo"));
//System.out.println(obj.get("releaseInfo"));
//System.out.println(obj.get("movieName"));
@Service
public class BoxOfficeServiceImpl implements BoxOfficeService{
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private PhotoRepository photoRepository;
	
	@Override
	public List<TodayBoxOffice> getTodayBoxOffice() {
		//猫眼票房API
		String url = "https://box.maoyan.com/promovie/api/box/second.json";
		List<TodayBoxOffice> todayBoxOfficeList = new ArrayList<TodayBoxOffice>();
        Connection con = Jsoup.connect(url);
        //请求头设置             
        con.header("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"); 
        con.header("cookie", "_lxsdk_cuid=167c07d1c60c8-0745fa3808eb37-3c604504-e1000-167c07d1c61c8; _lxsdk=3291D8C002A111E9ABC733D765948D5E4BC8501DCBB44EBE9F0FCD522EC10600; __guid=211801990.2374386180731500500.1545270584963.1387; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; __mta=55335700.1545722263468.1545724799982.1545725759233.5; monitor_count=2"); 
        con.ignoreContentType(true); 
        //解析请求结果
        Response resp;
		try {
			resp = con.execute();
			Map map = (Map)JSON.parse(resp.body());
			Map map2 = (Map)map.get("data");
			List<Map<String,Object>> movieList = (List<Map<String,Object>>) map2.get("list");
			for(Map<String,Object> obj : movieList) {
				Optional<Movie> optionalMovie = movieRepository.findById(Long.parseLong(obj.get("movieId").toString()));
				if(optionalMovie.isPresent()) {
					Movie movie = optionalMovie.get();
					List<Photo> photos = movie.getPhotos();
					System.out.println(movie);
					System.out.println(photos);
					TodayBoxOffice temp = new TodayBoxOffice("https://maoyan.com/films/" + obj.get("movieId").toString(),
							obj.get("movieName").toString(),
							obj.get("boxInfo").toString());
					temp.setMovie(movie);
					temp.setPhotos(photos);
					todayBoxOfficeList.add(temp);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return todayBoxOfficeList;
	}

}
