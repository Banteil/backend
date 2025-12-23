package com.example.board.reply.controller;

import com.example.board.reply.dto.ReplyDTO;
import com.example.board.reply.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
@Log4j2
public class ReplyController {

    private final ReplyService replyService;

    // =========================================================
    // 1. 댓글 등록 처리 (POST /reply/register)
    // =========================================================
    @PostMapping("/register")
    public String register(@Valid ReplyDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        log.info("Reply register 호출: dto={}", dto);
        Long bno = dto.getBno(); // Hidden 필드로 넘어온 게시글 번호

        if (bindingResult.hasErrors()) {
            log.warn("댓글 유효성 검사 오류 발생");
            redirectAttributes.addFlashAttribute("replyError", "댓글 내용을 모두 입력해주세요.");
            return "redirect:/board/read?bno=" + bno;
        }

        try {
            Long rno = replyService.register(bno, dto);
            redirectAttributes.addFlashAttribute("msg", "댓글이 성공적으로 등록되었습니다.");
        } catch (NoSuchElementException e) {
            log.error("댓글 등록 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "댓글 등록 실패: 해당 게시글을 찾을 수 없습니다.");
        }

        // 등록 후 게시글 상세 페이지로 돌아갑니다.
        return "redirect:/board/read?bno=" + bno;
    }

    // =========================================================
    // 2. 댓글 수정 처리 (POST /reply/modify)
    // =========================================================
    @PostMapping("/modify")
    public String modify(@Valid ReplyDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        log.info("Reply modify 호출: dto={}", dto);
        Long bno = dto.getBno();

        if (bindingResult.hasErrors() || dto.getRno() == null) {
            log.warn("댓글 수정 유효성 검사 오류 또는 rno 누락");
            redirectAttributes.addFlashAttribute("replyError", "댓글 내용이나 RNO가 누락되었습니다.");
            return "redirect:/board/read?bno=" + bno;
        }

        try {
            replyService.modify(dto);
            redirectAttributes.addFlashAttribute("msg", "댓글 " + dto.getRno() + "번이 성공적으로 수정되었습니다.");
        } catch (NoSuchElementException e) {
            log.error("댓글 수정 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "댓글 수정 실패: 해당 댓글을 찾을 수 없습니다.");
        }

        return "redirect:/board/read?bno=" + bno;
    }

    // =========================================================
    // 3. 댓글 삭제 처리 (POST /reply/remove)
    // =========================================================
    @PostMapping("/remove")
    public String remove(Long rno, Long bno, RedirectAttributes redirectAttributes) {

        log.info("Reply remove 호출: rno={}, bno={}", rno, bno);

        try {
            replyService.remove(rno);
            redirectAttributes.addFlashAttribute("msg", "댓글 " + rno + "번이 성공적으로 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            log.error("댓글 삭제 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "댓글 삭제 실패: 해당 댓글을 찾을 수 없습니다.");
        }

        return "redirect:/board/read?bno=" + bno;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/list/{bno}")
    public String getReplyList(@PathVariable("bno") Long bno, Model model) {
        log.info("댓글 목록 조각 요청: bno={}", bno);
        List<ReplyDTO> replies = replyService.getList(bno);
        model.addAttribute("replies", replies);
        model.addAttribute("replyCount", replies.size());
        return "fragments/reply::replyList";
    }
}