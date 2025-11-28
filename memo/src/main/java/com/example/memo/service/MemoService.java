package com.example.memo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.memo.dto.MemoDTO;
import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemoService {
    @Autowired
    private MemoRepository memoRepository;

    //전체 조회
    public List<MemoDTO> readAll() {
        List<Memo> memos = memoRepository.findAll();
        List<MemoDTO> list = memos.stream()
            .map(Memo::toDTO)
            .collect(Collectors.toList());

        return list;
        // Entity : service => repository, repository => service
        // ~~DTO : service => controller, controller => service
        // 리턴하기 전 Memo entity => MemoDTO로 변경 후 리턴
    }
}
