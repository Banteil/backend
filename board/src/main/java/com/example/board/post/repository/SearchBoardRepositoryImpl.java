package com.example.board.post.repository;

import java.util.List;
import java.util.stream.Collectors;

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
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

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

                // 1. 데이터 조회 쿼리 (SELECT) - Left Join 및 Group By 적용
                List<BoardDTO> content = getQuerydsl().createQuery()
                                .select(
                                                Projections.constructor(BoardDTO.class,
                                                                board.bno,
                                                                board.title,
                                                                board.content,
                                                                board.writer.name, // Member 엔티티의 name (writerName)
                                                                board.writer.email, // Member 엔티티의 email (writerEmail)

                                                                // 🔥 댓글 개수를 JOIN 기반으로 집계: reply.count() 사용
                                                                reply.count().coalesce(0L), // count()는 Long 타입으로 집계됨

                                                                board.createDateTime,
                                                                board.updateDateTime))
                                .from(board)
                                .leftJoin(board.writer, member) // 작성자 조인
                                .leftJoin(reply).on(reply.board.eq(board)) // 🔥 Reply 테이블을 LEFT JOIN
                                .where(predicate) // 검색 조건 Predicate 적용

                                // 🔥 Group By 절 추가: SELECT 절의 집계 함수(count)를 제외한 모든 필드를 그룹화
                                .groupBy(
                                                board.bno,
                                                board.title,
                                                board.content,
                                                board.writer.name,
                                                board.writer.email,
                                                board.createDateTime,
                                                board.updateDateTime // DTO 생성자 순서에 맞춰 그룹핑 필드 나열
                                )

                                // 2. 페이징 적용
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())

                                // 3. 정렬 적용
                                .orderBy(board.bno.desc())
                                .fetch();

                log.info("리스트 DTO : {}", content);

                // 4. 카운트 쿼리 (COUNT)
                // GROUP BY를 사용하지 않는 것이 효율적입니다.
                // 기존 쿼리는 정확히 Board 엔티티의 개수만 세기 때문에 그대로 유지하는 것이 최적입니다.
                Long total = getQuerydsl().createQuery()
                                .select(board.bno.count())
                                .from(board)
                                .leftJoin(board.writer, member)
                                .where(predicate) // 검색 조건 Predicate 적용
                                .fetchOne();

                // 5. Page 객체로 반환
                return PageableExecutionUtils.getPage(content, pageable, () -> total);
        }

        @Override
        public Object[] getBoardByBno(Long bno) {
                QBoard board = QBoard.board;
                QMember member = QMember.member;
                QReply reply = QReply.reply;

                JPQLQuery<Board> query = from(board)
                                .leftJoin(member).on(board.writer.eq(member))
                                .leftJoin(reply).on(reply.board.eq(board))
                                .where(board.bno.eq(bno));

                JPQLQuery<Tuple> tuple = query.select(board, member, reply.count());
                System.out.println(tuple);
                var result = tuple.fetchFirst();
                return result.toArray();
        }
}
