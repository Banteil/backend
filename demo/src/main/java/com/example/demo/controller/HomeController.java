package com.example.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



//TemplateInputException : html 없어서 오류

@Log4j2
@Controller
public class HomeController {
    // get 요청으로 들어올 때
    @GetMapping("/home")
    public void getHome() {
        log.info("Home 요청");
    }

    @GetMapping("/add")
    public String getAdd() {
        return "result";
    }

    @GetMapping("/calc")
    public void getCalc() {
        log.info("calc 요청");
    }


    @PostMapping("/calc")
    public void postCalc(int num1) {
        log.info("calc post {}", num1);
    }
    
    
}
