package com.example.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.LoginInfoDTO;

//TemplateInputException : html 없어서 오류

@Log4j2
@Controller
@RequestMapping("/member")
public class LoginController {
    // get 요청으로 들어올 때
    @GetMapping("/login")
    public void getLogin() {
        log.info("member/login 요청");
    }

    // @PostMapping("/login")
    // public String postLogin(LoginInfoDTO loginInfo, Model model) {
    //     log.info("member/login 요청 : {}",loginInfo.toString());
    //     model.addAttribute("loginInfo", loginInfo);
    //     return "member/login";
    // }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute("login") LoginInfoDTO loginInfo) {
        log.info("member/login 요청 : {}",loginInfo.toString());
        return "member/login";
    }
}
