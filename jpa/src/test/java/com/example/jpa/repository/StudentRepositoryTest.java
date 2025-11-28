package com.example.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Student;
import com.example.jpa.entity.constant.Grade;

@SpringBootTest
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void readTest()
    {
        Student result = studentRepository.findById(1L).get();
        System.out.println(result.toString());
    }

    @Test
    public void readAllTest()
    {
        List<Student> results = studentRepository.findAll();
        results.forEach((e) -> {
            System.out.println(e.toString());
        });
    }

    @Test
    public void deleteTest()
    {
        // studentRepository.delete(null);      
        studentRepository.deleteById(2L);      
    }

    @Test
    public void updateTest()
    {
        Optional<Student> result = studentRepository.findById(1L);

        Student student = result.get();
        student.changeName("춘향");

        // insert(c), update(u) 작업 시 호출
        studentRepository.save(student);
    }
    
    @Test
    public void insertTest()
    {
        Student student = Student.builder()
        .name("성춘향")
        .addr("부산")
        .gender("F")
        .grade(Grade.SENIOR)
        .build();

        // insert(c), update(u) 작업 시 호출
        studentRepository.save(student);

        // delete from ~ 호출
        // studentRepository.delete(student);
        // studentRepository.deleteById(student);

        // select * from where id = 1;
        //studentRepository.findById(1);
        // select * from;
        //studentRepository.findAll();
    }
}
