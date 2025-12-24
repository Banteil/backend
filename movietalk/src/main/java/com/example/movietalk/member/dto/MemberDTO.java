package com.example.movietalk.member.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.movietalk.member.entity.constant.Role;
import com.example.movietalk.movie.dto.ReviewDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long mid;
    private String email;
    private String nickname;
    private String password;
    private Role role;
    @Builder.Default
    private List<ReviewDTO> reviews = new ArrayList<>();

    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
