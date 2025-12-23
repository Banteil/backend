package com.example.board.reply.service;

import com.example.board.reply.dto.ReplyDTO;
import com.example.board.reply.entity.Reply;
import com.example.board.reply.repository.ReplyRepository;
import com.example.board.post.dto.BoardDTO;
import com.example.board.post.entity.Board; // Board 엔티티 임포트
import com.example.board.post.repository.BoardRepository; // BoardRepository 임포트

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper; // DTO <=> Entity 변환을 위해 ModelMapper 사용 가정

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository; // Board 엔티티 조회를 위해 필요
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    // =========================================================
    // 1. 댓글 등록 (Register / Insert)
    // =========================================================
    @Transactional
    public Long register(Long bno, ReplyDTO dto) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new NoSuchElementException("게시글 " + bno + "번을 찾을 수 없습니다."));
        Reply reply = modelMapper.map(dto, Reply.class);
        reply.setBoard(board);
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            reply.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Reply savedReply = replyRepository.save(reply);
        return savedReply.getRno();
    }

    // =========================================================
    // 2. 댓글 수정 (Modify)
    // =========================================================
    @Transactional
    public void modify(ReplyDTO dto) {
        Reply reply = replyRepository.findById(dto.getRno())
                .orElseThrow(() -> new NoSuchElementException("댓글 " + dto.getRno() + "번을 찾을 수 없습니다."));
        reply.setText(dto.getText());
        replyRepository.save(reply);
    }

    // =========================================================
    // 3. 댓글 삭제 (Remove)
    // =========================================================
    @Transactional
    public void remove(Long rno) {
        // 삭제 전 존재 여부 확인 (옵션)
        if (!replyRepository.existsById(rno)) {
            throw new NoSuchElementException("댓글 " + rno + "번을 찾을 수 없습니다.");
        }
        replyRepository.deleteById(rno);
    }

    @Transactional
    public List<ReplyDTO> getList(Long bno) {
        List<Reply> result = replyRepository.findByBoardBnoOrderByCreateDateTimeAsc(bno);

        return result.stream()
                .map(reply -> modelMapper.map(reply, ReplyDTO.class))
                .collect(Collectors.toList());
    }
}