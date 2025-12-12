package com.example.board.post.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.member.entity.Member;
import com.example.board.member.entity.QMember;
import com.example.board.member.repository.MemberRepository;
import com.example.board.post.dto.BoardDTO;
import com.example.board.post.dto.PageRequestDTO;
import com.example.board.post.dto.PageResultDTO;
import com.example.board.post.entity.Board;
import com.example.board.post.entity.QBoard;
import com.example.board.post.repository.BoardRepository;
import com.example.board.reply.dto.ReplyDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional
public class BoardService {
    private final BoardRepository bR;
    private final MemberRepository mR;
    private final ModelMapper mapper;

    // 🔥 Predicate 생성 로직을 Service 내 private 메서드로 구현
    private Predicate createSearchPredicate(PageRequestDTO dto) {
        log.info("createSearchPredicate {}", dto);
        BooleanBuilder builder = new BooleanBuilder();
        QBoard board = QBoard.board;
        QMember member = QMember.member;

        String type = dto.getType();
        String keyword = dto.getKeyword();

        // 2. 검색 조건 적용 (기존 로직 그대로)
        if (type != null && keyword != null && !keyword.trim().isEmpty()) {
            String searchKeyword = "%" + keyword.trim() + "%";

            BooleanBuilder searchBuilder = new BooleanBuilder();

            if (type.contains("t")) {
                searchBuilder.or(board.title.like(searchKeyword));
            }
            if (type.contains("a")) {
                searchBuilder.or(member.name.like(searchKeyword));
            }
            builder.and(searchBuilder);
        }

        return builder;
    }

    @Transactional(readOnly = true)
    public PageResultDTO<BoardDTO> getList(PageRequestDTO dto) {
        Predicate predicate = createSearchPredicate(dto);
        Page<BoardDTO> result = bR.getBoardPage(predicate, dto.toPageable());
        log.info("result {}", result.getContent());
        PageResultDTO<BoardDTO> pageResultDTO = PageResultDTO.<BoardDTO>withAll()
                .dtoList(result.getContent())
                .pageRequestDTO(dto)
                .totalCount(result.getTotalElements())
                .build();
        log.info("pageResultDTO {}", pageResultDTO);
        return pageResultDTO;
    }

    @Transactional(readOnly = true)
    public BoardDTO read(Long bno) {
        Board board = bR.getBoardWithReplies(bno)
                .orElseThrow(() -> new NoSuchElementException("게시글 번호 " + bno + "를 찾을 수 없습니다."));

        BoardDTO dto = mapper.map(board, BoardDTO.class);
        dto.setWriterName(board.getWriter().getName());
        dto.setWriterEmail(board.getWriter().getEmail());
        List<ReplyDTO> replyDtoList = board.getReplies().stream()
                .map(ReplyDTO::new)
                .toList();

        dto.setReplyCount((long) replyDtoList.size());
        dto.setReplies(replyDtoList);

        return dto;
    }

    public Long insert(BoardDTO dto) {
        Board board = mapper.map(dto, Board.class);
        board.setWriter(
                mR.findByEmail(dto.getWriterEmail())
                        .orElseThrow(() -> new NoSuchElementException(
                                "작성자 이메일 " + dto.getWriterEmail() + "에 해당하는 회원을 찾을 수 없습니다.")));
        Board savedBoard = bR.save(board);
        return savedBoard.getBno();
    }

    public Long update(BoardDTO dto) {
        Board board = bR.findById(dto.getBno())
                .orElseThrow(() -> new NoSuchElementException("게시글 번호 " + dto.getBno() + "를 찾을 수 없습니다."));
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        return board.getBno();
    }

    @Transactional
    public void remove(Long bno) {
        if (!bR.existsById(bno)) {
            throw new NoSuchElementException("삭제할 게시글 번호 " + bno + "를 찾을 수 없습니다.");
        }
        bR.deleteById(bno);
    }

}
