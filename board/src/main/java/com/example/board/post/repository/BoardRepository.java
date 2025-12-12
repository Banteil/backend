package com.example.board.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.example.board.post.dto.BoardDTO;
import com.example.board.post.entity.Board;

public interface BoardRepository
        extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<Board>, SearchBoardRepository {
    // on 구문 생략 기준 : 일치하는 컬럼
    @Query("select b, m from Board b join b.writer m")
    List<Object[]> getBoardWithWriterList();

    @Query("select b, r from Board b left join Reply r on r.board = b where b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value = "select b, m, count(r) from Board b left join b.writer m left join Reply r on r.board = b group by b", countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("select distinct b from Board b join fetch b.writer m left join fetch b.replies r where b.bno = :bno")
    Optional<Board> getBoardWithReplies(@Param("bno") Long bno);

    @Query(value = "select new com.example.board.post.dto.BoardDTO(b, m, count(r)) from Board b left join b.writer m left join Reply r on r.board = b group by b", countQuery = "select count(b) from Board b")
    Page<BoardDTO> getBoardListWithProjection(Pageable pageable);
}
