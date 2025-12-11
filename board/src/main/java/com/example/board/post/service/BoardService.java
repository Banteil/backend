package com.example.board.post.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.post.dto.BoardDTO;
import com.example.board.post.dto.PageRequestDTO;
import com.example.board.post.entity.Board;
import com.example.board.post.repository.BoardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional
public class BoardService {
    private final BoardRepository bR;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public Page<Board> getList(PageRequestDTO dto) {
        Page<Board> result = bR.findAll(dto.toPageable());
        return result;
    }

    public void getRow(Long bno) {

    }

    public Long insert(BoardDTO dto) {
        Board board = mapper.map(dto, Board.class);
        return bR.save(board).getBno();
    }

    public Long update(BoardDTO dto) {
        Board book = bR.findById(dto.getBno())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        mapper.map(dto, book);
        // return bookRepository.save(book).getId();
        return book.getBno();
    }

    public String delete(BoardDTO dto) {
        Board book = bR.findById(dto.getBno())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        String title = book.getTitle();
        bR.deleteById(dto.getBno());
        return title;
    }

}
