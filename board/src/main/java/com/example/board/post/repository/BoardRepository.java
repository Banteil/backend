package com.example.board.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board.post.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

}
