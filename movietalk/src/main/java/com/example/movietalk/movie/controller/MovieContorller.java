package com.example.movietalk.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.movietalk.common.dto.PageRequestDTO;
import com.example.movietalk.common.dto.PageResultDTO;
import com.example.movietalk.movie.dto.MovieDTO;
import com.example.movietalk.movie.service.MovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/movie")
@RequiredArgsConstructor
@Log4j2
@Controller
public class MovieContorller {
    private final MovieService movieService;

    @GetMapping("/list")
    public void getMovieList(PageRequestDTO pageRequestDTO, Model model) {
        log.info("영화 리스트 요청 {}", pageRequestDTO);
        PageResultDTO<MovieDTO> result = movieService.getMovieList(pageRequestDTO);
        model.addAttribute("result", result);
    }

    @GetMapping("/create")
    public void getCreate() {
        log.info("영화 추가 폼 요청");
    }

    @PostMapping("/create")
    public String postCreate(MovieDTO movieDTO, RedirectAttributes redirectAttributes) {
        log.info("영화 추가 요청 DTO: {}", movieDTO);
        Long mno = movieService.register(movieDTO);
        redirectAttributes.addFlashAttribute("msg", mno + "번 영화가 등록되었습니다.");
        return "redirect:/movie/list";
    }

    @GetMapping({ "/read", "/modify" })
    public void getMovieRead(@RequestParam("mno") Long mno,
            @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
            Model model) {
        log.info("영화 상세 조회 mno: {}", mno);

        MovieDTO movieDTO = movieService.getMovie(mno);

        model.addAttribute("dto", movieDTO);
        model.addAttribute("pageRequestDTO", requestDTO);
    }

    @PostMapping("/modify")
    public String postModify(MovieDTO movieDTO, RedirectAttributes redirectAttributes, PageRequestDTO pageRequestDTO) {
        log.info("영화 수정 요청 DTO: {}", movieDTO);
        Long mno = movieService.update(movieDTO);
        redirectAttributes.addFlashAttribute("msg", mno + "번 영화가 수정되었습니다.");
        redirectAttributes.addAttribute("mno", mno);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("size", pageRequestDTO.getSize());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        return "redirect:/movie/read";
    }

    @PostMapping("/remove")
    public String postRemove(@RequestParam("mno") Long mno, RedirectAttributes redirectAttributes,
            PageRequestDTO pageRequestDTO) {
        log.info("영화 삭제 요청 mno: {}", mno);
        // 서비스에서 DB 및 물리 파일 삭제 처리
        movieService.removeWithFiles(mno);
        redirectAttributes.addFlashAttribute("msg", mno + "번 영화가 삭제되었습니다.");

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("size", pageRequestDTO.getSize());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        return "redirect:/movie/list";
    }

}
