package com.example.book.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.book.dto.BookDTO;
import com.example.book.dto.PageRequestDTO;
import com.example.book.dto.PageResultDTO;
import com.example.book.service.BookService;

@RequiredArgsConstructor
@RequestMapping("/book")
@Log4j2
@Controller
public class BookController {
    private final BookService bookService;

    @GetMapping("/register")
    public void getRegister(Model model) {
        log.info("책 등록 요청");
        model.addAttribute("dto", new BookDTO());
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("dto") @Valid BookDTO dto,
            BindingResult result,
            RedirectAttributes rttr) {
        log.info("등록 요청 : {}", dto);

        // 유효성 검증 조건에 일치하지 않는 경우
        if (result.hasErrors()) {
            return "/book/register";
        }

        bookService.insert(dto);
        rttr.addFlashAttribute("msg", dto.getTitle() + " 도서가 등록되었습니다.");
        return "redirect:/book/register";
    }

    @GetMapping({ "/read", "/modify" })
    public void getReadAndModify(
            Long id,
            Model model,
            @RequestParam(value = "fromPage", required = false) String fromPage) {

        log.info("book id : {}", id);
        BookDTO dto = bookService.read(id);
        model.addAttribute("dto", dto);
        model.addAttribute("fromPage", fromPage);
    }

    @PostMapping("/modify")
    public String postModify(@ModelAttribute("dto") @Valid BookDTO dto,
            BindingResult result,
            @RequestParam(value = "fromPage", required = false) String fromPage, // 폼에서 전달된 fromPage를 받음
            RedirectAttributes rttr) {

        log.info("modify POST 요청: {}", dto);

        if (result.hasErrors()) {
            return "/book/modify";
        }

        Long id = bookService.update(dto);
        rttr.addFlashAttribute("msg", "ID " + id + "번 도서 정보가 성공적으로 수정되었습니다.");

        if ("list".equals(fromPage)) {
            return "redirect:/book/list";
        } else {
            rttr.addAttribute("id", id);
            return "redirect:/book/read";
        }
    }

    @PostMapping("/remove")
    public String postRemove(@RequestParam Long id, RedirectAttributes rttr) {
        log.info("remove POST 요청 ID: {}", id);
        String title = bookService.delete(id);
        rttr.addFlashAttribute("msg", title + " 도서가 삭제되었습니다.");
        return "redirect:/book/list";
    }

    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        Page<BookDTO> bookPage = bookService.readAll(pageRequestDTO);
        PageResultDTO<BookDTO> resultDTO = PageResultDTO.<BookDTO>withAll()
                .dtoList(bookPage.getContent())
                .pageRequestDTO(pageRequestDTO)
                .totalCount((int) bookPage.getTotalElements())
                .build();

        model.addAttribute("list", bookPage.getContent());
        model.addAttribute("page", resultDTO);
        return "book/list";
    }
}
