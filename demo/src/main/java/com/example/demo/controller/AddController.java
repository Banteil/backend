package com.example.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//TemplateInputException : html 없어서 오류

@Log4j2
@Controller
// @RequestMapping("/member")
public class AddController {
    // get 요청으로 들어올 때
    @GetMapping("/exam3")
    public void getExam3() {
        log.info("exam3 요청");
    }
}
