package com.example.student.dto;

import java.time.LocalDateTime;

import com.example.student.entity.constant.Grade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class StudentDTO {
    private Long id;

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Size(max = 50, message = "이름은 최대 50자까지 입력할 수 있습니다.")
    private String name;

    @NotBlank(message = "주소는 필수 항목입니다.")
    @Size(max = 255, message = "주소는 최대 255자까지 입력할 수 있습니다.")
    private String addr;
    private String gender;
    private Grade grade;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
