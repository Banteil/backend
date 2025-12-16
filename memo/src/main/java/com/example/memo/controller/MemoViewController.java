package com.example.memo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.memo.dto.MemoDTO;
import com.example.memo.service.MemoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@RequestMapping("/rmemo")
@Log4j2
@Controller
public class MemoViewController {
    private final MemoService memoService;

    @GetMapping("/list")
    public String listPage() {
        return "rmemo/list";
    }

    @GetMapping("/read")
    public String readPage() {
        return "rmemo/read";
    }

    @GetMapping("/insert")
    public String insertPage() {
        return "rmemo/insert";
    }

    @GetMapping("/modify")
    public String modifyPage(@RequestParam Long id, Model model) {
        MemoDTO dto = memoService.read(id);
        if (dto == null) {
            return "redirect:/rmemo/list";
        }
        model.addAttribute("dto", dto);
        return "rmemo/modify";
    }
}
