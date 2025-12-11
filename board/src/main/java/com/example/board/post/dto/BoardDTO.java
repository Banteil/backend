package com.example.board.post.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long bno;
    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title;
    @NotBlank(message = "내용은 필수 항목입니다.")
    private String content;
    private String email;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
