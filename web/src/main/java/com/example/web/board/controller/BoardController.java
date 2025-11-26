package com.example.web.board.controller;

import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.web.board.dto.BoardDTO;

// 게시판 기능
// controller, dto, repository, service, entity

@Log4j2
@Controller
@RequestMapping("/board")
public class BoardController {
    @GetMapping("/list")
    public void getList(Model model) {
        log.info("list 요청");
        //BoardDTO dto = new BoardDTO(1L, "스프링 부트", "홍길동", LocalDate.now());

        List<BoardDTO> list = new ArrayList<>();
        for (Long i = 1L; i < 21; i++) {
            //Builder 패턴 적용
            BoardDTO dto = BoardDTO.builder()
            .id(i)
            .title("스프링 부트 " + i)
            .writer("홍길동")
            .regDate(LocalDateTime.now())
            .build();

            list.add(dto);
        }

        model.addAttribute("dto", list);
    }

    @GetMapping("/read")
    public void getRead(@RequestParam Long id) {
        log.info("read 요청 {}", id);
    }
}
