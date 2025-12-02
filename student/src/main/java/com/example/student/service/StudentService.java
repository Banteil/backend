package com.example.student.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        List<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.ASC,"id"));

        List<StudentDTO> list = students.stream()
        .map(Student::toDTO)
        .collect(Collectors.toList());

        return list;
    }

    public Page<StudentDTO> readAll(Pageable pageable) {
        Page<Student> studentPage = studentRepository.findAll(pageable);
        Page<StudentDTO> dtoPage = studentPage.map(Student::toDTO);
        return dtoPage;
    }

    public Long update(StudentDTO dto)
    {
        // 1) 수정 대상 찾기
        Student student = studentRepository.findById(dto.getId())
                                        .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));
        // 2) 🌟 ModelMapper를 사용하여 DTO의 변경된 값을 Entity에 덮어씌움 (핵심) 🌟
        modelMapper.map(dto, student);        
        return studentRepository.save(student).getId();
    }

    public void delete(Long id)
    {
        studentRepository.deleteById(id);
    }
}
