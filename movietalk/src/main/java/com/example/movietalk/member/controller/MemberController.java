package com.example.movietalk.member.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@Log4j2
public class MemberController {

    @GetMapping("/login")
    public void login() {
        log.info("login page 요청");
    }

    @GetMapping("/register")
    public void register() {
        log.info("register page 요청");
    }
}