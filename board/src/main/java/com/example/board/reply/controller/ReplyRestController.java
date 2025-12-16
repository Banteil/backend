package com.example.board.reply.controller;

import com.example.board.reply.dto.ReplyDTO;
import com.example.board.reply.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
@Log4j2
public class ReplyRestController {

    private final ReplyService replyService;

    // =========================================================
    // 1. 댓글 등록 처리 (POST /replies)
    // =========================================================
    @PostMapping("")
    public ResponseEntity<Map<String, Long>> register(@Valid @RequestBody ReplyDTO dto,
            BindingResult bindingResult) {

        log.info("REST Reply register 호출: dto={}", dto);

        if (bindingResult.hasErrors()) {
            log.warn("댓글 유효성 검사 오류 발생");
            // 400 Bad Request 에러와 함께 유효성 검사 실패 응답
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long bno = dto.getBno();
        Long rno = replyService.register(bno, dto);

        // 생성된 댓글 번호를 JSON으로 반환 (200 OK)
        return ResponseEntity.ok(Map.of("rno", rno));
    }

    // =========================================================
    // 2. 댓글 수정 처리 (PUT /replies/{rno})
    // =========================================================
    @PutMapping("/{rno}")
    public ResponseEntity<Map<String, String>> modify(@PathVariable("rno") Long rno,
            @Valid @RequestBody ReplyDTO dto,
            BindingResult bindingResult) {

        log.info("REST Reply modify 호출: rno={}, dto={}", rno, dto);

        // 경로의 rno를 DTO에 설정
        dto.setRno(rno);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            replyService.modify(dto);
            return ResponseEntity.ok(Map.of("result", "success"));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // =========================================================
    // 3. 댓글 삭제 처리 (DELETE /replies/{rno})
    // =========================================================
    @DeleteMapping("/{rno}")
    public ResponseEntity<Map<String, String>> remove(@PathVariable("rno") Long rno) {

        log.info("REST Reply remove 호출: rno={}", rno);

        try {
            replyService.remove(rno);
            return ResponseEntity.ok(Map.of("result", "success"));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list/{bno}")
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno) {
        log.info("댓글 목록 조회: bno={}", bno);
        List<ReplyDTO> replies = replyService.getList(bno); // 서비스에서 목록 조회
        return ResponseEntity.ok(replies);
    }
}