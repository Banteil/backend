package com.example.memo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memo.dto.MemoDTO;
import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional
public class MemoService {
    // @Autowired
    // private MemoRepository memoRepository;
    // @Autowired
    // private ModelMapper modelMapper;

    private final MemoRepository memoRepository;
    private final ModelMapper modelMapper;

    // 전체 조회
    @Transactional(readOnly = true)
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

    // 하나 조회
    @Transactional(readOnly = true)
    public MemoDTO read(Long id) {
        // Memo memo = memoRepository.findById(id).get();
        // Memo memo = null;
        // Optional<Memo> result = memoRepository.findById(id);
        // if(result.isPresent()){
        // memo = result.get();
        // }

        // NoSuchElementException
        Memo memo = memoRepository.findById(id).orElseThrow();
        // entity => dto 변환 후 리턴
        return modelMapper.map(memo, MemoDTO.class);
    }

    public Long modify(MemoDTO dto) {
        // 1) 수정 대상 찾기
        Memo memo = memoRepository.findById(dto.getId()).orElseThrow();
        // 2) 변경
        memo.setText(dto.getText());
        return memo.getId();
    }

    public void remove(Long id) {
        memoRepository.deleteById(id);
    }

    public Long insert(MemoDTO dto) {
        // dto => entity
        Memo memo = modelMapper.map(dto, Memo.class);
        return memoRepository.save(memo).getId();
    }
}
