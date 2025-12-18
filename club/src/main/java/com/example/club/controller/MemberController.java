package com.example.club.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j2;

@RequestMapping("/member")
@Log4j2
@Controller
public class MemberController {
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

    @ResponseBody
    @GetMapping("/auth")
    public Authentication getAuthInfo() {
        log.info("auth 요청");
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication;
    }
}
