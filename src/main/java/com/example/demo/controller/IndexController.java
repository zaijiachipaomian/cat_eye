package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.TodayBoxOffice;
import com.example.demo.entity.User;
import com.example.demo.service.BoxOfficeService;

@Controller
public class IndexController {
	
	@Autowired
	BoxOfficeService boxOfficeService;
	
	@ResponseBody
	@GetMapping("/index_data/todayBoxOffice")
	public Object getIndexInfor() {
		//猫眼票房API
		String url = "https://box.maoyan.com/promovie/api/box/second.json";
        Connection con = Jsoup.connect(url);
        //请求头设置             
        con.header("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"); 
        con.header("cookie", "_lxsdk_cuid=167c07d1c60c8-0745fa3808eb37-3c604504-e1000-167c07d1c61c8; _lxsdk=3291D8C002A111E9ABC733D765948D5E4BC8501DCBB44EBE9F0FCD522EC10600; __guid=211801990.2374386180731500500.1545270584963.1387; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; __mta=55335700.1545722263468.1545724799982.1545725759233.5; monitor_count=2"); 
        con.ignoreContentType(true); 
        //解析请求结果
        Response resp = null;
		try {
			resp = con.execute();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resp.body();
	}
	
	@GetMapping("/")
	public String index(HttpServletRequest request,HttpServletResponse response) {
		HttpSession session =request.getSession();
		User user=(User) session.getAttribute("user");
		if(user!=null) {
			Cookie c=new Cookie("user_id",user.getId().toString());
			Cookie c1=new Cookie("username", user.getUsername());
			//会话级cookie，关闭浏览器失效
			c.setMaxAge(-1);
			response.addCookie(c);
			response.addCookie(c1);
		}
		return "index";
	}
}
