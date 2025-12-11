package com.example.board.reply.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board.reply.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
