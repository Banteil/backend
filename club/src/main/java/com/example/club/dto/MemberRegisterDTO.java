package com.example.club.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRegisterDTO {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
    private String password;
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(min = 2, message = "이름은 최소 2자 이상 입력해주세요.")
    private String name;
}