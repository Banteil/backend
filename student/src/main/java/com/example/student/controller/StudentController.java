package com.example.student.controller;

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

import com.example.student.dto.StudentDTO;
import com.example.student.entity.constant.Grade;
import com.example.student.service.StudentService;

@RequiredArgsConstructor
@RequestMapping("/student")
@Log4j2
@Controller
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/register")
    public void getRegister(Model model) {
        log.info("학생 등록 요청");  
        model.addAttribute("dto", new StudentDTO());
        model.addAttribute("grades", Grade.values());
    }
    
    @PostMapping("/register")
    public String postRegister(@ModelAttribute("dto") @Valid StudentDTO dto, BindingResult result, RedirectAttributes rttr) {
        log.info("등록 요청 : {}", dto);

        //유효성 검증 조건에 일치하지 않는 경우
        if(result.hasErrors()){
            return "/student/register";
        }

        Long id = studentService.insert(dto);
        rttr.addFlashAttribute("msg",  dto.getName() + " 학생이 등록되었습니다.");
        return "redirect:/student/register";
    }    

    @GetMapping({"/read", "/modify"})
    public void getReadAndModify(Long id, Model model) {
        log.info("student id : {}", id);
        StudentDTO readDto = studentService.read(id);
        model.addAttribute("dto", readDto);
        model.addAttribute("grades", Grade.values());
    }    

    @PostMapping("/modify")
    public String postModify(@ModelAttribute("dto") @Valid StudentDTO dto, BindingResult result, RedirectAttributes rttr) {
        log.info("modify POST 요청: {}", dto);
        //유효성 검증 조건에 일치하지 않는 경우
        if(result.hasErrors()){
            return "/student/modify";
        }
        Long id = studentService.update(dto); 
        rttr.addAttribute("id", id); 
        rttr.addFlashAttribute("msg", id + "번 학생 정보가 성공적으로 수정되었습니다.");
        return "redirect:/student/read?id={id}"; 
    }

    @PostMapping("/remove")
    public String postRemove(@RequestParam Long id, RedirectAttributes rttr) {
        log.info("remove POST 요청 ID: {}", id);
        studentService.delete(id);
        rttr.addFlashAttribute("msg", "삭제가 완료되었습니다.");
        return "redirect:/student/list"; 
    }

    @GetMapping("/list")
    public String getList(Model model, 
                        @RequestParam(value = "page", defaultValue = "1") int page, // page 파라미터를 1부터 받음
                        @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageableDefault) {
        
        // 사용자가 입력한 1-based page 번호를 0-based index로 변환
        int pageIndex = page - 1; 
        if (pageIndex < 0) {
            pageIndex = 0; // 최소 페이지 인덱스는 0으로 보장
        }

        // 기존 pageableDefault의 size, sort 정보를 사용하여 새로운 PageRequest 생성
        Pageable pageable = PageRequest.of(pageIndex, 
                                        pageableDefault.getPageSize(), 
                                        pageableDefault.getSort());
        
        log.info("학생 목록 요청 - 페이지 인덱스: {}", pageable.getPageNumber()); // 0-based 출력
        Page<StudentDTO> studentsPage = studentService.readAll(pageable);
        
        // Page 객체는 그대로 list라는 이름으로 모델에 추가
        // Thymeleaf 코드에서는 여전히 page.number (0-based)를 사용해야 함
        model.addAttribute("list", studentsPage);
        // model.addAttribute("maxPage", 10); // 이 값은 실제 totalPages로 대체하는 것이 좋습니다.
        
        return "student/list";
    }
}
