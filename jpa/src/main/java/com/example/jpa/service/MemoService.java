package com.example.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jpa.entity.Memo;
import com.example.jpa.repository.MemoRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemoService {
    @Autowired
    private MemoRepository memoRepository;

    //전체 조회
    public void readAll() {
        List<Memo> memos = memoRepository.findAll();
        
    }
}
