package com.example.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Memo;

@SpringBootTest
public class MemoRepositoryTest {
    @Autowired
    private MemoRepository memoRepository;

    @Test
    public void readTest()
    {
        Memo result = memoRepository.findById(1L).get();
        System.out.println(result.toString());
    }

    @Test
    public void readAllTest()
    {
        List<Memo> results = memoRepository.findAll();
        for (Memo memo : results) {
            System.out.println(memo.toString());
        }        
    }

    @Test
    public void deleteTest()
    {
        // studentRepository.delete(null);      
        memoRepository.deleteById(2L);      
    }

    @Test
    public void updateTest()
    {
        Optional<Memo> result = memoRepository.findById(1L);

        Memo memo = result.get();

        // insert(c), update(u) 작업 시 호출
        memoRepository.save(memo);
    }
    
    @Test
    public void insertTest()
    {
        Memo memo = Memo.builder()
        .memoText("테스트")
        .build();

        // insert(c), update(u) 작업 시 호출
        memoRepository.save(memo);
    }
}
