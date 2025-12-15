package com.example.board.reply.dto;

import java.time.LocalDateTime;

import com.example.board.reply.entity.Reply;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {

    private Long rno;
    @NotBlank(message = "내용은 필수 항목입니다.")
    private String text;
    @NotBlank(message = "작성자명은 필수 항목입니다.")
    private String replayer;
    private Long bno;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;

    public ReplyDTO(Reply reply) {
        this.rno = reply.getRno();
        this.text = reply.getText();
        this.replayer = reply.getReplayer();
        this.bno = reply.getBoard().getBno();
        this.createDateTime = reply.getCreateDateTime();
        this.updateDateTime = reply.getUpdateDateTime();
    }
}