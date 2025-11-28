package com.example.memo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoDTO {
    private Long id;
    private String text;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
