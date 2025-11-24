package com.example.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//TemplateInputException : html 없어서 오류

@Log4j2
@Controller
public class BoardController {
    // get 요청으로 들어올 때
    @GetMapping("/board/add")
    public void getAdd() {
        log.info("board/add 요청");
    }    

    @GetMapping("/board/add2")
    public void getAdd2() {
        log.info("board/add2 요청");
    }    
}
