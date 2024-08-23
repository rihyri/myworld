package com.community.board.message;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private int id;

    @Size(max=200)
    private String title;

    @Size(max=200)
    private String content;

    private String senderName;

    @Size(max=200)
    private String receiverName;

    private LocalDateTime createDate;

    private boolean viewed;

    public static MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getTitle(),
                message.getContent(),
                message.getSender().getNickname(),
                message.getReceiver().getNickname(),
                message.getCreateDate(),
                message.isViewed()
        );
    }
}
