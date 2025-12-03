package com.example.book.dto;

import java.time.LocalDateTime;

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
    private String isbn;
    // @NotBlank(message = "주소는 필수 항목입니다.")
    // @Size(max = 255, message = "주소는 최대 255자까지 입력할 수 있습니다.")
    private String title;
    private int price;
    private String author;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
