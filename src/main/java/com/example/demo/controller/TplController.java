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
}
