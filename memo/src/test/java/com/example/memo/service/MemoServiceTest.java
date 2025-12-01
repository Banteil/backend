package com.example.memo.service;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.memo.dto.MemoDTO;

@Disabled
@SpringBootTest
public class MemoServiceTest {
    @Autowired
    private MemoService memoService;

    @Test
    public void readAllTest()
    {
        List<MemoDTO> result = memoService.readAll();
        result.forEach((dto) -> {
            System.out.println(dto);
        });
    }
}
