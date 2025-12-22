package com.example.board.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.board.member.dto.MemberRegisterDTO;
import com.example.board.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequestMapping("/member")
@Log4j2
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService mS;

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

    @GetMapping("/register")
    public void getRegister() {
        log.info("register 요청");
    }

    @ResponseBody
    @PostMapping("/register")
    public ResponseEntity<String> postRegister(@Valid @RequestBody MemberRegisterDTO dto, BindingResult bindingResult) {
        log.info("REST 가입 요청 : " + dto);

        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.warn("가입 유효성 검사 실패: {}", errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }
        try {
            String email = mS.register(dto);
            return ResponseEntity.ok(email);
        } catch (Exception e) {
            log.error("가입 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
