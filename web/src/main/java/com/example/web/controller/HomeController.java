package com.example.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class HomeController {
    @GetMapping("/")
    public String getIndex() {
        log.info("index 진입");
        return "redirect:/home";
    }

    @GetMapping("/home")
    public void getHome() {
        log.info("Home 요청 : {}");
    }
}
