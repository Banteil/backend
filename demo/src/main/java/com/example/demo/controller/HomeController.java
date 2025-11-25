package com.example.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.UserInfoDTO;

// import jakarta.servlet.http.HttpServletRequest;



//TemplateInputException : html 없어서 오류

@Log4j2
@Controller
public class HomeController {
    // get 요청으로 들어올 때
    @GetMapping("/")
    public String getIndex(RedirectAttributes rttr) {
        log.info("index 진입");
        rttr.addAttribute("bno", 10); //리다이렉트 시 데이터 전달(주소에 표시)
        rttr.addFlashAttribute("money", 1000);  //리다이렉트 시 데이터 전달(1회성)
        return "redirect:/home";
    }

    @GetMapping("/home")
    public void getHome(int bno) {
        log.info("Home 요청 : {}", bno);
    }

    @GetMapping("/add")
    public String getAdd(@RequestParam int num1, @RequestParam String op, @RequestParam int num2, @RequestParam int result, Model model) {
        log.info("사칙연산 {} {} {} = {}",num1,op,num2,result);
        model.addAttribute("num1", num1);
        model.addAttribute("op", op);
        model.addAttribute("num2", num2);
        model.addAttribute("result", result);
        return "exam3";
    }

    @GetMapping("/calc")
    public void getCalc() {
        log.info("calc 요청");
    }

    @PostMapping("/calc")
    public void postCalc(int num1, int num2) {
        log.info("calc post {} {}", num1, num2);
    }

    @GetMapping("/info")
    public String getInfo(Model model) {
        log.info("info GET 요청 - 템플릿 로드");
        model.addAttribute("userinfo", new UserInfoDTO()); 
        return "info";
    }

    @PostMapping("/info")
    public String postInfo(UserInfoDTO userinfo, Model model) {
        log.info("info post {}", userinfo.toString());
        model.addAttribute("userinfo", userinfo);    
        // 템플릿 이름(info.html)을 반환하여 재렌더링
        return "info";  
    }

    // @PostMapping("/info")
    // public void postInfo(HttpServletRequest request) {
    //     log.info("info post {}", request.toString());
    //     String username = request.getParameter("username");
    //     log.info("username : {}", username);
    // }
}
