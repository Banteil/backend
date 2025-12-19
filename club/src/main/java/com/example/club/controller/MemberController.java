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
    @GetMapping("/profile")
    public void getProfile() {
        log.info("proflie 요청");
    }

    @GetMapping("/manager")
    public String getManager() {
        log.info("manager 요청");
        return "/manager/info";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        log.info("admin 요청");
        return "/admin/manage";
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
