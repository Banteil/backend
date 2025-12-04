package com.example.book.dto;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    @NotBlank(message = "코드는 필수 항목입니다.")
    private String isbn;
    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title;
    @NotNull(message = "가격은 필수 항목입니다.")
    @Range(min = 0, max = 10000000, message = "가격은 0 ~ 10000000 사이입니다.")
    private Integer price;
    @NotBlank(message = "저자는 필수 항목입니다.")
    private String author;
    private String description;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
