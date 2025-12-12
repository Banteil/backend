package com.example.board.post.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.example.board.member.entity.QMember;
import com.example.board.post.dto.BoardDTO;
import com.example.board.post.entity.Board;
import com.example.board.post.entity.QBoard;
import com.example.board.reply.entity.QReply;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public List<Board> list() {
        log.info("board + member FETCH JOIN을 통해 N+1 문제 해결");

        QBoard board = QBoard.board;
        QMember member = QMember.member; // Board와 연관된 Member (작성자)

        // 쿼리 작성 시작
        List<Board> result = from(board)
                // 1. Board와 Member를 JOIN FETCH를 사용하여 조인
                .join(board.writer, member).fetchJoin() // writer는 Board 엔티티 내 Member 필드라고 가정
                // 2. 필요한 조건 (예: bno가 0보다 큰 것)
                .where(board.bno.gt(0))
                // 3. 페이징 및 정렬은 Service/Controller에서 Pageable 객체로 처리
                // 여기서는 단순 전체 리스트를 가져옵니다.
                .fetch();

        // 주의: QuerydslRepositorySupport는 Pageable 적용이 복잡합니다.
        // 여기서는 간단히 Board 리스트를 반환하거나 DTO로 변환하여 반환합니다.

        // 예시로 List<Board>를 반환한다고 가정합니다.
        return (List) result;
    }

    @Override
    public Page<BoardDTO> getBoardPage(Predicate predicate, Pageable pageable) {
        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QReply reply = QReply.reply;

        // 1. 데이터 조회 쿼리 (SELECT)
        List<BoardDTO> content = getQuerydsl().createQuery()
                .select(
                        Projections.constructor(BoardDTO.class,
                                board.bno,
                                board.title,
                                board.content,
                                board.writer.name, // Member 엔티티의 name (writerName)
                                board.writer.email, // Member 엔티티의 email (writerEmail)
                                // 🔥 서브쿼리를 이용해 댓글 개수 집계
                                JPAExpressions.select(reply.rno.count().coalesce(0L)) // null 방지를 위해 coalesce(0L) 사용
                                        .from(reply)
                                        .where(reply.board.eq(board)),
                                board.createDateTime,
                                board.updateDateTime))
                .from(board)
                .leftJoin(board.writer, member) // 작성자 조인
                .where(predicate) // 🔥 검색 조건 Predicate 적용
                // 2. 페이징 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                // 3. 정렬 적용
                .orderBy(board.bno.desc()) // Pageable의 정렬을 사용하거나 명시 (여기서는 bno 내림차순으로 임시 고정)
                .fetch();
        log.info("리스트 DTO : {}", content);

        // 4. 카운트 쿼리 (COUNT)
        Long total = getQuerydsl().createQuery()
                .select(board.bno.count())
                .from(board)
                .leftJoin(board.writer, member)
                .where(predicate) // 🔥 검색 조건 Predicate 적용
                .fetchOne();

        // 5. Page 객체로 반환
        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }
}
