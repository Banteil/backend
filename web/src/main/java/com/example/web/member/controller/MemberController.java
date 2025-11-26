package com.example.web.member.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.web.member.dto.LoginDTO;
import com.example.web.member.dto.RegisterDTO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

// 멤버 기능
// controller, dto, repository, service, entity

@Log4j2
@Controller
@RequestMapping("/member")
public class MemberController {
    @GetMapping("/login")
    public void getLogin() {
        log.info("login 요청");
    }

    // HttpSession : http 프로토콜 단점 해결
    // 로그인, 장바구니
    // 서버 쪽에 정보 저장

    // 브라우저 정보 저장
    // 쿠키

    @PostMapping("/login")
    public String postLogin(LoginDTO dto, HttpSession session) {
        log.info("post login : {}", dto);
        session.setAttribute("loginDto", dto);
        return "redirect:/";
    }
    
    @GetMapping("/register")
    public void getRegister(RegisterDTO dto) {
        log.info("register 요청");
    }

    @PostMapping("/register")
    public String postRegister(@Valid RegisterDTO dto, BindingResult result) {
        log.info("post register : {}", dto);
        if(result.hasErrors()){
            return "/member/register";
        }
        return "redirect:/member/login";
    }
}
