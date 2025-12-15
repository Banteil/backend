package com.example.board.post.dto;

import org.springframework.data.domain.PageRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.board.member.entity.QMember;
import com.example.board.post.entity.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 20;
    @Builder.Default
    private String type = null;
    @Builder.Default
    private String keyword = null;

    public Pageable toPageable() {
        int pageIndex = (page > 0) ? page - 1 : 0;

        return PageRequest.of(
                pageIndex,
                this.size,
                Sort.by(Sort.Direction.DESC, "bno") // 정렬 기준 적용
        );
    }

    public String getLinkParams() {
        StringBuilder builder = new StringBuilder();
        builder.append("page=").append(this.page);
        if (this.type != null && !this.type.isEmpty()) {
            builder.append("&type=").append(this.type);
        }
        if (this.keyword != null && !this.keyword.isEmpty()) {
            builder.append("&keyword=").append(this.keyword);
        }
        return builder.toString();
    }

    public Predicate createSearchPredicate() {
        BooleanBuilder builder = new BooleanBuilder();
        QBoard board = QBoard.board;
        QMember member = QMember.member;

        if (type != null && keyword != null && !keyword.trim().isEmpty()) {
            String searchKeyword = "%" + keyword.trim() + "%";

            BooleanBuilder searchBuilder = new BooleanBuilder();

            if (type.contains("t")) {
                searchBuilder.or(board.title.like(searchKeyword));
            }
            if (type.contains("c")) {
                searchBuilder.or(board.content.like(searchKeyword));
            }
            if (type.contains("w")) {
                searchBuilder.or(member.name.like(searchKeyword));
            }
            builder.and(searchBuilder);
        }
        return builder;
    }
}
