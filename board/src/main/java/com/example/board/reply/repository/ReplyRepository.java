package com.example.board.reply.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.board.reply.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Modifying
    @Query("delete from Reply r where r.board.bno = :bno")
    public void deleteByBno(Long bno);

    List<Reply> findByBoardBnoOrderByCreateDateTimeAsc(Long bno);
}
