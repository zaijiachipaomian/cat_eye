package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserService userService ;
    // 管理员登陆页面
    @ResponseBody
    @PostMapping("/login")
    public Object login(HttpServletRequest request, String phone, String pwd) {
        //
        System.out.println("管理员登陆 "+phone+ " pwd "+ pwd );

        Admin admin = new Admin();
        Response response = new Response();
        admin.phone = phone;
        admin.pwd = pwd;
        // 排除空值
        // 403
        if (admin.phone == null || pwd == null || phone.equals("") || pwd.equals("")){
            response.code = 403;
            response.data = "forbidden";
            return response;
        }


        User login = userService.login(phone, pwd);
        if (login == null){
            response.code = 403;
            response.data = "手机号码或者密码错误,请重新登陆";
            return response;
        }
        request.getSession().setAttribute(ADMIN,admin);


        response.code = HTTP_OK;
        response.data = "ok";
        return response;
    }

     class Admin {
        String phone;
        String pwd;
    }

     public class Response {
        int code; // 状态码
        Object data; // 返回信息

         public int getCode() {
             return code;
         }

         public void setCode(int code) {
             this.code = code;
         }

         public Object getData() {
             return data;
         }

         public void setData(Object data) {
             this.data = data;
         }

         @Override
         public String toString() {
             System.out.println("hello world toString ...... "+JSON.toJSONString(this));
             return JSON.toJSONString(this);
         }
     }
}
