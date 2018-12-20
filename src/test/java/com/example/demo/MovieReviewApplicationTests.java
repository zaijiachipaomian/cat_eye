package com.example.demo;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;
import com.example.demo.entity.News;
import com.example.demo.entity.Person;
import com.example.demo.entity.Photo;
import com.example.demo.entity.Type;
import com.example.demo.repository.MovieRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieReviewApplicationTests {

	@Autowired
	MovieRepository movieRepository;
	
	@Test
	public void contextLoads2() {
		
		Movie movie = new Movie("海王",
				"https://p0.meituan.net/movie/c106904da68edd848afd4a320976d051346321.jpg",
				"温子仁",
				"乔夫·琼斯",
				"李路迪",
				"美国",
				"2018-12-18",
				"134分钟",
				"海王爸爸好厉害",
				"正在热映");
//		movie.setPhotos(photos);
//		movie.setNewsList(newsList);
//		movie.setComments(comments);
//		movie.setPrizes(prizes);
//		movie.setMovieRoles(movieRoles);
//		movie.setPersons(persons);
//		movie.setTpyes(tpyes);
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/9403fa3dba59733ae508c288c3314dd0439945.jpg"));
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/5d08cde7241c47c29098fb4f6e412454431755.jpg@126w_126h_1e_1c"));
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/1a7c3163e69bbea88d4a376e1989a750448007.jpg@126w_126h_1e_1c"));
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/67c1b11171efce62bddd38fa3b45e932462532.jpg@126w_126h_1e_1c"));
		movie.addPhoto(new Photo("https://p0.meituan.net/movie/a2dd21ca43c5293d9bbbe665942d3d05411150.jpg@126w_126h_1e_1c"));
		movie.addNews(new News("《海王》劈浪斩海狂收13亿，环保主义备受热议", "/films/news/53168"));
		movie.addNews(new News("《海王》劈浪斩海狂收13亿，饭制海报惊艳眼球！环保主义备受热议", "/films/news/53130"));
		movie.addNews(new News("海王金装人偶长这样，像穿玉米装的徐锦江", "/films/news/53092"));
		movie.addPerson(new Person("https://p1.meituan.net/moviemachine/c40fd02a43037cdc83003d0c6a019456195561.jpg@128w_170h_1e_1c",
							"温子仁", "男", "中国", new Date(), ""));
	}
	
	@Test
	public void contextLoads() {
		Movie movie = new Movie();
		movie.setName("海王");
		movie.setPoster("https://p0.meituan.net/movie/c106904da68edd848afd4a320976d051346321.jpg");
		movie.setDirector("温子仁");
		movie.setWriter("乔夫·琼斯");
		movie.setMainActor("李路迪");
		Type type = new Type();
		Type type2 = new Type();
		type.setName("虚幻");
		type2.setName("探险");
		movie.addType(type);
		movie.addType(type2);
		Comment comment = new Comment();
		Comment comment2 = new Comment();
		comment.setDate(new Date());
		comment.setContent("很好看！可以");
		comment2.setDate(new Date());
		comment2.setContent("不错");
		movie.addComment(comment);
		movie.addComment(comment2);
		movieRepository.save(movie);
	}

}

