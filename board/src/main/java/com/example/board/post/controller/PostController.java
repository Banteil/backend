package com.example.board.post.controller;

import java.util.NoSuchElementException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.board.post.dto.BoardDTO;
import com.example.board.post.dto.PageRequestDTO;
import com.example.board.post.dto.PageResultDTO;
import com.example.board.post.service.BoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@RequestMapping("/board")
@Log4j2
@Controller
public class PostController {
    private final BoardService bS;

    @GetMapping("/list")
    public String getList(PageRequestDTO requestDTO, Model model) {
        log.info("get list 호출 : {}", requestDTO);
        PageResultDTO<BoardDTO> result = bS.getList(requestDTO);
        model.addAttribute("result", result);
        model.addAttribute("list", result.getDtoList());
        model.addAttribute("requestDTO", requestDTO);
        return "board/list";
    }

    @GetMapping("/read")
    public String getRead(@RequestParam("bno") Long bno, PageRequestDTO requestDTO, Model model) {
        log.info("read 호출: bno={}, requestDTO={}", bno, requestDTO);
        try {
            BoardDTO dto = bS.read(bno);
            model.addAttribute("dto", dto);
            model.addAttribute("requestDTO", requestDTO);
            return "board/read";
        } catch (NoSuchElementException e) {
            log.error("게시글 조회 실패: {}", e.getMessage());
            return "redirect:/board/list";
        }
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        log.info("registerForm 호출");
        model.addAttribute("boardDTO", new BoardDTO());
        return "board/register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid BoardDTO dto, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, Model model) {
        log.info("registerProcess 호출: dto={}", dto);

        if (bindingResult.hasErrors()) {
            log.warn("유효성 검사 오류 발생: {}", bindingResult.getAllErrors());
            model.addAttribute("boardDTO", dto);
            return "board/register";
        }

        Long bno = bS.register(dto);
        redirectAttributes.addFlashAttribute("msg", "게시글 " + bno + "번이 성공적으로 등록되었습니다.");
        return "redirect:/board/read?bno=" + bno;
    }

    @GetMapping("/modify")
    public String getModify(@RequestParam("bno") Long bno,
            @ModelAttribute PageRequestDTO requestDTO,
            Model model) {
        log.info("modifyForm 호출: bno={}, requestDTO={}", bno, requestDTO);
        try {
            BoardDTO dto = bS.read(bno);
            model.addAttribute("dto", dto);
            model.addAttribute("requestDTO", requestDTO);
            return "board/modify";
        } catch (NoSuchElementException e) {
            log.error("수정할 게시글 조회 실패: {}", e.getMessage());
            return "redirect:/board/list";
        }
    }

    // @PreAuthorize("authentication.name == #dto.writerEmail")
    @PostMapping("/modify")
    public String postModify(BoardDTO dto,
            @ModelAttribute PageRequestDTO requestDTO,
            RedirectAttributes redirectAttributes) {

        log.info("modifyProcess 호출: dto={}, requestDTO={}", dto, requestDTO);
        Long bno = bS.update(dto);
        redirectAttributes.addFlashAttribute("msg", "게시글 " + bno + "번이 성공적으로 수정되었습니다.");
        return "redirect:/board/read?" + requestDTO.getLinkParams() + "&bno=" + bno;
    }

    @PostMapping("/remove")
    public String postRemove(@RequestParam("bno") Long bno,
            RedirectAttributes redirectAttributes) {
        log.info("removeProcess 호출: bno={}", bno);
        try {
            bS.remove(bno);
            redirectAttributes.addFlashAttribute("msg", "게시글 " + bno + "번이 성공적으로 삭제되었습니다.");
            return "redirect:/board/list";
        } catch (NoSuchElementException e) {
            log.error("삭제할 게시글을 찾을 수 없음: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("msg", "삭제하려는 게시글을 찾을 수 없습니다.");
            return "redirect:/board/list";
        }
    }
}
