package com.example.book.dto;

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
    private int size = 10;
    @Builder.Default
    private String direction = "DESC";
    @Builder.Default
    private String sort = "id";

    public Pageable toPageable() {
        // 1. 1-기반 페이지를 0-기반 인덱스로 변환 (page > 0 이도록 보장)
        int pageIndex = (page > 0) ? page - 1 : 0;

        // 2. 정렬 방향 설정
        Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // 3. PageRequest 객체 생성 및 반환
        return PageRequest.of(
                pageIndex,
                this.size,
                Sort.by(sortDirection, this.sort) // 정렬 기준 적용
        );
    }
}
