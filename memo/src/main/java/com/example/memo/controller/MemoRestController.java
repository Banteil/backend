package com.example.memo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.DeleteMapping;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

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

    @PostMapping("/insert")
    public ResponseEntity<Long> postCreate(@RequestBody MemoDTO dto) {
        Long id = memoService.insert(dto);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    // Rest에서만 쓰이는 개념
    @PutMapping("/modify")
    public ResponseEntity<Long> putModify(@RequestBody MemoDTO dto) {
        log.info("메모 수정 요청: {}", dto);
        var id = memoService.modify(dto);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        memoService.remove(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
