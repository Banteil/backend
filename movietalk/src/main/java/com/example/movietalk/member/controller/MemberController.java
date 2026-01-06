package com.example.movietalk.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.movietalk.member.dto.MemberAuthDTO;
import com.example.movietalk.member.dto.MemberDTO;
import com.example.movietalk.member.service.MemberUserDetailsService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberUserDetailsService memberService;

    @GetMapping("/login")
    public void login() {
        log.info("login page 요청");
    }

    @GetMapping("/register")
    public void register() {
        log.info("register page 요청");
    }

    @PostMapping("/register")
    public String postRegister(@Valid MemberDTO memberDTO, BindingResult result) {
        log.info("회원가입 요청 {}", memberDTO);

        if (result.hasErrors()) {
            return "/member/register";
        }

        try {
            Long mid = memberService.join(memberDTO);
            return "redirect:/member/login";
        } catch (Exception e) {
            log.info(e.getMessage());
            return "/member/register";
        }
    }

    @GetMapping("/profile")
    public void getProfile() {
        log.info("profile page 요청");
    }

    @GetMapping("/edit")
    public void getEdit() {
        log.info("edit page 요청");
    }

    @PostMapping("/modifyNickname")
    public String modifyNickname(@AuthenticationPrincipal MemberAuthDTO authDTO,
            @RequestParam("nickname") String nickname,
            RedirectAttributes redirectAttributes) {

        // 1. DB 정보 수정
        memberService.modifyNickname(authDTO.getEmail(), nickname);
        authDTO.setNickname(nickname); // 세션 객체 내부 값 변경

        // 3. 새로운 Authentication 객체 생성 및 ContextHolder에 등록 (중요)
        // 기존 권한(getAuthorities)을 그대로 유지하면서 업데이트된 authDTO를 principal로 설정합니다.
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                authDTO,
                null, // 비밀번호는 이미 인증되었으므로 null
                authDTO.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        log.info("Session updated for user: " + authDTO.getEmail() + ", new nickname: " + nickname);

        redirectAttributes.addFlashAttribute("msg", "닉네임이 성공적으로 수정되었습니다.");
        return "redirect:/member/profile";
    }

    @PostMapping("/modifyPassword")
    public String modifyPassword(@AuthenticationPrincipal MemberAuthDTO authDTO,
            @RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword,
            RedirectAttributes redirectAttributes) {
        try {
            memberService.modifyPassword(authDTO.getEmail(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("msg", "비밀번호가 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/edit";
        }

        return "redirect:/member/profile";
    }

    @ResponseBody
    @GetMapping("/auth")
    public Authentication getAuth() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication;
    }

}