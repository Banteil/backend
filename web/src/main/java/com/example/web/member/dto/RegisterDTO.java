package com.example.web.member.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    @Length(min=4, max=8, message = "아이디는 4~8자리 사이로 작성해야 합니다.")
    @NotBlank(message = "아이디는 4~8자리 사이로 작성해야 합니다.")
    private String id;

    @Length(min=4, max=8, message = "비밀번호는 4~8자리 사이로 작성해야 합니다.")
    @NotEmpty(message = "비밀번호는 4~8자리 사이로 작성해야 합니다.")
    private String password;

    @Email(message = "이메일 형식에 맞춰 작성해야 합니다.")
    @NotEmpty(message = "이메일 형식에 맞춰 작성해야 합니다.")
    private String email;
}
