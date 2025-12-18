package com.example.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@RequestMapping("/sample")
@Log4j2
@Controller
public class SampleController {
    // http:localhost:8080: 모두에게 개방
    // sample/guest : 모두에게 개방
    // sample/member : 멤버에게 개방
    // sample/admin : 관리자에게 개방

    @GetMapping("/guest")
    public void getGuest() {
        log.info("guest 요청");
    }

    @GetMapping("/member")
    public void getMember() {
        log.info("member 요청");
    }

    @GetMapping("/admin")
    public void getAdmin() {
        log.info("admin 요청");
    }

    @GetMapping("/login")
    public void getLogin() {
        log.info("login 요청");
    }
}
