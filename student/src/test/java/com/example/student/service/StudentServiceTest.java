package com.example.student.service;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.student.dto.StudentDTO;
import com.example.student.entity.constant.Grade;

@Disabled
@SpringBootTest
public class StudentServiceTest {    
    @Autowired
    private StudentService studentService;    

    @Test
    public void testInsert(){
        StudentDTO dto = StudentDTO.builder()
        .name("홍길동")
        .gender("M")
        .addr("종로")
        .grade(Grade.FRESHMAN)
        .build();

        System.out.println(studentService.insert(dto));
    }

    @Test
    public void testRead()
    {       
        System.out.println(studentService.read(1L));
    }

    @Test
    public void testReadAll()
    {       
        List<StudentDTO> students = studentService.readAll();
        String formattedOutput = String.join("\n", students.stream()
                                                            .map(Object::toString)
                                                            .collect(Collectors.toList()));
        System.out.println(formattedOutput);
    }

    @Test
    public void testUpdate()
    {       
        StudentDTO dto = StudentDTO.builder()
        .name("김갑환")
        .gender("M")
        .addr("서울")
        .grade(Grade.SOPHOMORE)
        .build();
        Long newId = studentService.insert(dto);
        dto.setId(newId);
        dto.setName("이진주");
        dto.setGender("F");
        dto.setGrade(Grade.JUNIOR);

        Long id = studentService.update(dto);   
        System.out.println(studentService.read(id));
    }

    @Test
    public void testDelete()
    {       
        List<StudentDTO> students = studentService.readAll();
        Long id = students.get(students.size() - 1).getId();        
        studentService.delete(id);
    }
}
