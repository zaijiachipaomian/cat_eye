package com.example.demo;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonbTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Movie;
import com.example.demo.entity.MovieRole;
import com.example.demo.entity.News;
import com.example.demo.entity.Person;
import com.example.demo.entity.Photo;
import com.example.demo.entity.Prize;
import com.example.demo.entity.Type;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.MovieRoleRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.PhotoRepository;
import com.example.demo.repository.PrizeRepository;
import com.example.demo.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieReviewApplicationTests {

	@Autowired
	MovieRepository movieRepository;
	@Autowired
	PhotoRepository photoRepository;
	@Autowired
	PrizeRepository prizeRepository;
	@Autowired
	NewsRepository newsRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	MovieRoleRepository movieRoleRepository;
	@Autowired
	UserRepository userRepository;
	
	
	@Test
	public void contextLoads9() {
		Movie movie = movieRepository.findById(248215L).get();
		Page<Comment> comment;
		Pageable pageable = new PageRequest(0,5);
		comment=commentRepository.findByMovie(movie, pageable);
		System.out.println(comment.getContent());
	}
	
	//分页测试
	@SuppressWarnings("deprecation")
	@Test
	public void contextLoads8() {
		Page<Comment> comment;
		Pageable pageable = new PageRequest(0,5);
		int size=pageable.getPageSize();
		int pageNumber=pageable.getPageNumber();
		Movie movie=movieRepository.findById(248215L).get();
		comment=commentRepository.findByMovie(movie, pageable);
		System.out.println("size="+size);
		System.out.println("pageNumber="+pageNumber);
		System.out.println("comment="+comment.getContent());
	}
	
	//网络请求
	@Test
	public void contextLoads7() {
		String url = "https://box.maoyan.com/promovie/api/box/second.json";
        Connection con = Jsoup.connect(url);
        //请求头设置，特别是cookie设置               
        con.header("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"); 
        con.ignoreContentType(true); 
        //解析请求结果
        Response resp;
		try {
			resp = con.execute();
			System.out.println(resp.body());
			Map map = (Map)JSON.parse(resp.body());
			Map map2 = (Map)map.get("data");
			List<Map<String,Object>> movieList = (List<Map<String,Object>>) map2.get("list");
			for(Map<String,Object> obj : movieList) {
				System.out.println(obj.get("movieId"));
				System.out.println(obj.get("sumBoxInfo"));
				System.out.println(obj.get("boxInfo"));
				System.out.println(obj.get("releaseInfo"));
				System.out.println(obj.get("movieName"));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
	}

	@Test  //多对多测试
	public void contextLoads6() {
		//从电影获得类型
		System.out.println("从电影获得类型");
		Movie movie = movieRepository.findById(1L).get();
		System.out.println(movie.getTypes());
		//从类型获得电影
		System.out.println("从类型获得电影");
		Type type = movie.getTypes().get(0);
		System.out.println(type);
		List<Movie> movies = type.getMovies();
		System.out.println(movies);
		//从电影获得人物
		System.out.println("从电影获得人物");
		System.out.println(movie.getPersons());
		//从人物获得电影
		System.out.println("从人物获得电影");
		System.out.println(movie.getPersons().get(0).getMovies());
	}
	
	@Test  //一对多测试
	public void contextLoads4() {
		System.out.println("照片");
		Photo photo = photoRepository.findById(10L).get();
		System.out.println(photo);
		System.out.println(photo.getMovie());
		System.out.println(photo.getMovie().getPhotos());
		System.out.println("奖项");
		Prize prize = prizeRepository.findById(20L).get();
		System.out.println(prize);
		System.out.println(prize.getMovie());
		System.out.println(prize.getMovie().getPrizes());
		System.out.println("资讯");
		News news = newsRepository.findById(6L).get();
		System.out.println(news);
		System.out.println(news.getMovie());
		System.out.println(news.getMovie().getNewsList());
		System.out.println("评论");
		Comment comment = commentRepository.findById(2L).get();
		System.out.println(comment);
		System.out.println(comment.getMovie());
		System.out.println(comment.getMovie().getComments());
		System.out.println("电影角色");
		MovieRole movieRole = movieRoleRepository.findById(4L).get();
		System.out.println(movieRole);
		System.out.println(movieRole.getMovie());
		System.out.println(movieRole.getMovie().getMovieRoles());
		
	}
	
	//得到电影后，调用GET方法测试
	@Test
	public void contextLoads3() {
		Movie movie = movieRepository.findById(1L).get();
		System.out.println(movie);
		System.out.println(movie.getComments());
		System.out.println(movie.getNewsList());
		System.out.println(movie.getPhotos());
		System.out.println(movie.getPrizes());
		System.out.println(movie.getMovieRoles());
		System.out.println(movie.getTypes());
		System.out.println(movie.getPersons());
	}
	
	//级联插入测试
	@Test
	public void contextLoads5() {
		//用户、电影、评论，这样可以保证数据是这样的
		//  id   content  date              score movie_id  user_id
     //		29	很不错	2018-12-20 17:00:08	10	   1  	     28
		Movie movie = movieRepository.findById(1L).get();
		User user = new User();
		user.setUsername("13538628500");
		user.setPassword("123456");
		Comment comment = new Comment(new Date(), 10F, "很不错");
		user.addComment(comment);
		user = userRepository.save(user);
		movie.addComment(comment);
		movieRepository.save(movie);
//		
//		movie.addComment(comment);
//		userRepository.save(user);
	}
	
	
	//级联插入测试
	@Test
	public void contextLoads2() {
		
		Movie movie = new Movie("海王",
				"https://p0.meituan.net/movie/c106904da68edd848afd4a320976d051346321.jpg",
				"美国",
				new Date(),
				"134分钟",
				"海王爸爸好厉害",
				(float) 123);
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/9403fa3dba59733ae508c288c3314dd0439945.jpg"));
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/5d08cde7241c47c29098fb4f6e412454431755.jpg@126w_126h_1e_1c"));
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/1a7c3163e69bbea88d4a376e1989a750448007.jpg@126w_126h_1e_1c"));
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/67c1b11171efce62bddd38fa3b45e932462532.jpg@126w_126h_1e_1c"));
		movie.addPhoto(new Photo("https://p0.meituan.net/movie/a2dd21ca43c5293d9bbbe665942d3d05411150.jpg@126w_126h_1e_1c"));
		movie.addNews(new News("《海王》劈浪斩海狂收13亿，环保主义备受热议", "/films/news/53168","https://p1.meituan.net/movie/0d73818502a3949cb4aa279e75000f4f215026.jpg@140w_86h_1e_1c"));
		movie.addNews(new News("《海王》劈浪斩海狂收13亿，饭制海报惊艳眼球！环保主义备受热议", "/films/news/53130","https://p1.meituan.net/movie/0d73818502a3949cb4aa279e75000f4f215026.jpg@140w_86h_1e_1c"));
		movie.addNews(new News("海王金装人偶长这样，像穿玉米装的徐锦江", "/films/news/53092","https://p1.meituan.net/movie/0d73818502a3949cb4aa279e75000f4f215026.jpg@140w_86h_1e_1c"));
		movie.addPerson(new Person("https://p1.meituan.net/moviemachine/c40fd02a43037cdc83003d0c6a019456195561.jpg@128w_170h_1e_1c","温子仁", "男", "中国", "2018-12-26", "印尼导演"));
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/9403fa3dba59733ae508c288c3314dd0439945.jpg@465w_258h_1e_1c"));
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/5d08cde7241c47c29098fb4f6e412454431755.jpg@126w_126h_1e_1c"));
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/9403fa3dba59733ae508c288c3314dd0439945.jpg@465w_258h_1e_1c"));
		movie.addPhoto(new Photo("https://p1.meituan.net/movie/67c1b11171efce62bddd38fa3b45e932462532.jpg@126w_126h_1e_1c"));
		movie.addPhoto(new Photo("https://p0.meituan.net/movie/a2dd21ca43c5293d9bbbe665942d3d05411150.jpg@126w_126h_1e_1c"));
		movie.addPrize(new Prize("第67届奥斯卡金像奖", "https://p1.meituan.net/movie/ae0de4faa2366f9c23405c361387fe6f4358.jpg@50w_50h_1e_1c", "提名：最佳影片 / 最佳男主角 / 最佳改编剧本 / 最佳摄影 / 最佳音响效果 / 最佳剪辑 / 最佳原创音乐"));
		movie.addPrize(new Prize("第19届日本电影学院奖", "https://p0.meituan.net/movie/04d8bf2467f29ea0b72491587f8b34f95687.jpg@50w_50h_1e_1c", "获奖：最佳外语片"));
		movie.addPrize(new Prize("第42届土星奖", "https://p1.meituan.net/movie/346b977c9348f0c13dc74415ca8294ed4945.jpg@50w_50h_1e_1c", "提名：最佳DVD/蓝光套装"));
		movie.addType(new Type("剧情"));
		movie.addType(new Type("爱情"));
		movie.addMovieRole(new MovieRole("导演", 11L));
		movie.addMovieRole(new MovieRole("编辑", 11L));
		movie.addComment(new Comment(new Date(),9.5F, "好看"));
		movie.addComment(new Comment(new Date(),10F, "不错"));
		movieRepository.save(movie);
	}
	
	//无视我
	@Test
	public void contextLoads() {
		Movie movie = new Movie();
		movie.setName("海王");
		movie.setPoster("https://p0.meituan.net/movie/c106904da68edd848afd4a320976d051346321.jpg");
//		movie.setDirector("温子仁");
//		movie.setWriter("乔夫·琼斯");
//		movie.setMainActor("李路迪");
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

