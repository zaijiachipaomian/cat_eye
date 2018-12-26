package com.example.demo.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        System.out.println(handler);
        //String sessionId = request.getRequestedSessionId();
        //System.out.println(sessionId);
        //request.getSession().getAttribute(name)
        
        
        String id = request.getSession().getId();
        System.out.println(id);
        
        //等待登陆
        //request.getSession().setAttribute("user", value);
        //request.getSession().getAttribute("user");
        
//        Cookie[] cookies =  request.getCookies();
//        String value = null;
//        if(cookies != null){
//            for(Cookie cookie : cookies){
//                if(cookie.getName().equals("MySessionId")){
//                    value =  cookie.getValue();
//                }
//            }
//        }
//        
//        HttpSession session = request.getSession();
//        System.out.println(value);
//        if(value == null) {
//        	String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 9).toLowerCase();
//        	Cookie cookie = new Cookie("MySessionId", uuid);
//        	cookie.setPath("/");
//        	cookie.setMaxAge(3600);
//			response.addCookie(cookie);
//        }
//        if (request.getSession().getAttribute("user") == null) {
//            response.sendRedirect("/toLogin");
//            return false;
//        }
        return true;
    }
}
