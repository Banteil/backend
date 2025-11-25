package com.example.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/board")
public class BoardController {
    @GetMapping("/list")
    public void getList() {
        log.info("list 요청");
    }
}
