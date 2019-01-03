package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// 模板类 返回的是html5文档...
@Controller
public class TplController {


    @GetMapping("/films/{id}")
    public String films(){
        return "films";
    }


    @GetMapping("/person/{id}")
    public String persons(){
        return "persons";
    }
    @GetMapping("/films")
    public String filmsTotla(){
        return "films_totals";
    }

    @GetMapping("/user/tpl")
    public String userTpl(){
        return "user";
    }


    @GetMapping("/user/comment")
    public  String comments(){
        System.out.println("comments");
        return "comment";
    }


    @GetMapping("/board")
    public String board(){
        System.out.println("/board .......  ");
        return "board";
    }
}
