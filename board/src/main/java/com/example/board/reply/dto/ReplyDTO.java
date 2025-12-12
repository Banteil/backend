package com.example.board.reply.dto;

import java.time.LocalDateTime;

import com.example.board.reply.entity.Reply;
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
    private String text;
    private String replayer;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;

    public ReplyDTO(Reply reply) {
        this.rno = reply.getRno();
        this.text = reply.getText();
        this.replayer = reply.getReplayer();
        this.createDateTime = reply.getCreateDateTime();
        this.updateDateTime = reply.getUpdateDateTime();
    }
}