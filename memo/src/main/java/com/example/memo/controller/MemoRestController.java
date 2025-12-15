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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.memo.dto.MemoDTO;
import com.example.memo.service.MemoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@RequestMapping("/rest")
@Log4j2
@RestController
public class MemoRestController {
    private final MemoService memoService;

    @GetMapping("/list")
    public List<MemoDTO> getList(Model model) {
        log.info("전체 메모 요청");
        List<MemoDTO> list = memoService.readAll();
        return list;
    }

    @GetMapping({ "/{id}" })
    public MemoDTO getRead(@PathVariable Long id) {
        log.info("memo id : {}", id);
        MemoDTO dto = memoService.read(id);
        return dto;
    }
}
