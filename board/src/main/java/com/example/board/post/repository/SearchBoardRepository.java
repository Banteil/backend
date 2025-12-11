package com.example.board.post.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.board.post.entity.Board;

@Repository
public interface SearchBoardRepository {
    List<Board> list();
}
