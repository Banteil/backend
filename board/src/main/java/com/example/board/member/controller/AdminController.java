package com.example.board.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@RequestMapping("/admin")
@Log4j2
@Controller
public class AdminController {
    @GetMapping("/manage")
    public void getAdminManage() {
        log.info("admin manage 요청");
    }
}
