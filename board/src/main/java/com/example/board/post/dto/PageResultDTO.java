package com.example.board.post.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Data;

@Data
public class PageResultDTO<E> {
    private List<E> dtoList;
    private List<Integer> pageNumList;
    private PageRequestDTO pageRequestDTO;
    private boolean prev, next;
    private long totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResultDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = totalCount;

        // 1. 페이지 블록의 끝 번호 계산 (10 단위)
        int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;

        // 2. 페이지 블록의 시작 번호 계산
        int start = end - 9;

        // 3. 실제 전체 페이지 수 (마지막 페이지 번호) 계산
        int last = (int) (Math.ceil(totalCount / (double) pageRequestDTO.getSize()));

        // 4. end 보정 (end가 last보다 크면 last로 설정)
        end = end > last ? last : end;

        // 5. 이전/다음 버튼 활성화 여부 계산
        this.prev = start > 1;
        this.next = end < last; // 마지막 블록이 아니면 next 활성화

        // 6. 페이지 번호 목록 생성
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        if (prev) {
            this.prevPage = start - 1;
        }
        if (next) {
            this.nextPage = end + 1;
        }

        // ✅ 수정: 전체 페이지 수는 last와 같습니다.
        this.totalPage = last;

        this.current = pageRequestDTO.getPage();
    }
}
