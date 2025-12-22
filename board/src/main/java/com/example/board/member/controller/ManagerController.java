package com.example.board.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@RequestMapping("/manager")
@Log4j2
@Controller
public class ManagerController {
    @GetMapping("/info")
    public void getManagerInfo() {
        log.info("manager info 요청");
    }
}
