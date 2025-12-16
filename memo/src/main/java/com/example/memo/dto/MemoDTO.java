package com.example.memo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoDTO {
    private Long id;

    @NotBlank(message = "빈 메모는 작성하실 수 없습니다.")
    @Size(max = 255, message = "메모 내용은 최대 255자까지 입력할 수 있습니다.")
    private String text;

    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
