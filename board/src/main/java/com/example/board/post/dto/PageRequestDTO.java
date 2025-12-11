package com.example.board.post.dto;

import org.springframework.data.domain.PageRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
}
