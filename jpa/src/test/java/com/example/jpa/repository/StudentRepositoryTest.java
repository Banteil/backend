package com.example.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Student;

@SpringBootTest
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;
    
    @Test
    public void insertTest()
    {
        Student student = Student.builder()
        .name("홍길동")
        .addr("부산")
        .gender("M")
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
