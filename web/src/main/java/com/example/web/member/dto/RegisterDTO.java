package com.example.web.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

// import org.hibernate.validator.constraints.Length;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    // @Length(min=4, max=8, message = "아이디는 4~8자리 사이로 작성해야 합니다.")
    // @NotBlank(message = "아이디는 4~8자리 사이로 작성해야 합니다.")
    // 영어 대소문자, 숫자만 포함되는 5~12자리 문자
    @Pattern(regexp = "(?=^[A-Za-z])(?=.+[\\d])[A-Za-z\\d]{5,12}", message = "아이디는 영어 대소문자, 숫자만 포함되는 5~12자리 문자로 작성해야 합니다.")
    private String id;

    // @Length(min=4, max=8, message = "비밀번호는 4~8자리 사이로 작성해야 합니다.")
    // @NotEmpty(message = "비밀번호는 4~8자리 사이로 작성해야 합니다.")
    // 영어 대소문자, 숫자, !#$%만 포함되는 5~12자리 문자
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[\\d])(?=.*[!#$%])[A-Za-z\\d!#$%]{5,12}", message = "비밀번호는 영어 대소문자, 숫자, !#$%만 포함되는 5~12자리 문자로 작성해야 합니다.")
    private String password;

    @Email(message = "이메일 형식에 맞춰 작성해야 합니다.")
    @NotEmpty(message = "이메일을 작성해 주세요.")
    private String email;

    @Pattern(regexp ="^[가-힣]{2,6}$", message = "이름은 2~6자리로 작성하셔야 합니다.")
    private String name;

    @Max(value = 120, message ="나이는 최대 120살 이하여야 합니다.")
    @Min(value = 1, message ="나이는 최소 1살 이상이어야 합니다.")
    @NotNull(message = "나이는 필수 요소입니다.")
    private Integer age;
}
