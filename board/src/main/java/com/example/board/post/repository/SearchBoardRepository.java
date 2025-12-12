package com.example.board.post.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.board.post.dto.BoardDTO;
import com.example.board.post.entity.Board;
import com.querydsl.core.types.Predicate;

@Repository
public interface SearchBoardRepository {
    List<Board> list();

    Page<BoardDTO> getBoardPage(Predicate predicate, Pageable pageable);
}
