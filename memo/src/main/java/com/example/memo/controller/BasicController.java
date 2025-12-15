package com.example.memo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memo.dto.MemoDTO;
import com.example.memo.service.MemoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Log4j2
@RestController
public class BasicController {
    private final MemoService mS;

    @GetMapping("/hello")
    public String getHello() {
        return "Hello World";
    }

    @GetMapping("/sample1")
    public MemoDTO getSample1() {
        MemoDTO dto = mS.read(1L);
        return dto;
    }

    @GetMapping("/sample/read/{id}")
    public MemoDTO getRead(@PathVariable Long id) {
        MemoDTO dto = mS.read(id);
        return dto;
    }

    @GetMapping("/sample/list")
    public List<MemoDTO> getList(Model model) {
        log.info("전체 메모 요청");
        List<MemoDTO> list = mS.readAll();
        return list;
    }
}
