package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    public static final String ADMIN = "admin";
    public static final int HTTP_OK = 200;

    @GetMapping("")
    public String homePage(HttpServletRequest request) {
        System.out.println("out 请求管理员页面.....");
//        Admin admin = (Admin) request.getSession().getAttribute(ADMIN);
//
//        if (admin == null) {
//            return "redirect:/admin/login";
//        }

        return "admin/index";
    }

    // 管理员登陆页面
    @ResponseBody
    @PostMapping("/login")
    public Object login(HttpServletRequest request, String name, String pwd) {
        //
        Admin admin = new Admin();
        admin.name = name;
        admin.pwd = pwd;
        request.setAttribute(ADMIN, admin);

        Response response = new Response();
        response.code = HTTP_OK;
        response.data = "ok";
        return response;
    }

    static class Admin {
        String name;
        String pwd;
    }

    static class Response {
        int code; // 状态码
        Object data; // 返回信息
    }
}
