package com.example.student.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.student.dto.StudentDTO;
import com.example.student.entity.Student;
import com.example.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public Long insert(StudentDTO dto)
    {
        // dto => entity
        Student student = modelMapper.map(dto, Student.class);
        return studentRepository.save(student).getId();
    }

    public StudentDTO read(Long id)
    {
        // NoSuchElementException
        Student student = studentRepository.findById(id).orElseThrow();
        // entity => dto 변환 후 리턴
        return modelMapper.map(student, StudentDTO.class);
    }

    public List<StudentDTO> readAll() {
        //select ~~~ order by id desc
        List<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));

        List<StudentDTO> list = students.stream()
        .map(Student::toDTO)
        .collect(Collectors.toList());

        return list;
    }

    public Long update(StudentDTO dto)
    {
        // 1) 수정 대상 찾기
        Student student = studentRepository.findById(dto.getId()).orElseThrow();
        // 2) 변경
        student.setGrade(dto.getGrade());
        student.setName(dto.getName());
        
        return studentRepository.save(student).getId();
    }

    public void delete(Long id)
    {
        studentRepository.deleteById(id);
    }


}
