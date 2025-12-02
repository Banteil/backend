package com.example.memo.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.memo.dto.MemoDTO;
import com.example.memo.service.MemoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@RequestMapping("/memo")
@Log4j2
@Controller
public class MemoController {
    private final MemoService memoService;

    @GetMapping("/list")
    public void getList(Model model) {
        log.info("전체 메모 요청");
        List<MemoDTO> list = memoService.readAll();
        model.addAttribute("list", list);
    }    

    @GetMapping({"/read", "/modify"})
    public void getRead(@RequestParam Long id, Model model) {
        log.info("memo id : {}", id);
        MemoDTO dto = memoService.read(id);
        model.addAttribute("dto", dto);
    }    

    @PostMapping("/modify")
    public String postModify(MemoDTO dto, BindingResult result, RedirectAttributes rttr) {
        log.info("modify POST 요청: {}", dto);
        //유효성 검증 조건에 일치하지 않는 경우
        if(result.hasErrors()){
            return "/memo/modify";
        }
        Long id = memoService.modify(dto); 
        rttr.addAttribute("id", id);         
        return "redirect:/memo/read?id={id}"; 
    }

    @PostMapping("/remove")
    public String postRemove(@RequestParam Long id, RedirectAttributes rttr) {
        log.info("remove POST 요청 ID: {}", id);
        memoService.remove(id);
        rttr.addFlashAttribute("msg", "삭제가 완료되었습니다.");
        return "redirect:/memo/list"; 
    }

    @GetMapping("/create")
    public String getCreate(MemoDTO dto, Model model) {
        log.info("추가 페이지 요청");
        model.addAttribute("dto", new MemoDTO()); 
        return "memo/create";
    }  

    @PostMapping("/create")
    public String postCreate(@Valid MemoDTO dto, BindingResult result, RedirectAttributes rttr) {
        log.info("추가 요청 : {}", dto);

        //유효성 검증 조건에 일치하지 않는 경우
        if(result.hasErrors()){
            return "/memo/create";
        }

        Long id = memoService.insert(dto);
        rttr.addFlashAttribute("msg", id + "번 메모가 추가되었습니다.");
        return "redirect:/memo/list";
    }  
}
