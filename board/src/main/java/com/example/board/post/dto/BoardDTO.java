package com.example.board.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.board.member.entity.Member;
import com.example.board.post.entity.Board;
import com.example.board.reply.dto.ReplyDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
    private String password;
    private String writerName;
    private String writerEmail;
    private Long replyCount;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;

    // 🔥 댓글 목록 필드 추가
    private List<ReplyDTO> replies;

    public BoardDTO(Long bno, String title, String content, String password,
            String writerName, String writerEmail, Long replyCount,
            LocalDateTime createDateTime, LocalDateTime updateDateTime) {
        this.bno = bno;
        this.title = title;
        this.content = content;
        this.password = password;
        this.writerName = writerName;
        this.writerEmail = writerEmail;
        this.replyCount = replyCount;
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
    }

    public BoardDTO(Board board, Member member, Long replyCount) {
        this.bno = board.getBno();
        this.title = board.getTitle();
        this.password = board.getPassword();
        this.content = board.getContent();
        this.writerName = member.getName();
        this.writerEmail = member.getEmail();
        this.replyCount = replyCount;
        this.createDateTime = board.getCreateDateTime();
        this.updateDateTime = board.getUpdateDateTime();
    }
}
