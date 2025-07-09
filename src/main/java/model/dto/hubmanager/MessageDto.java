package model.dto.hubmanager;

import lombok.Data;
import java.time.LocalDateTime;

public class MessageDto {
    @Data
    public class MessageCreateDto {
        private Long senderId;
        private Long receiverId;
        private String content;
    }

    @Data
    public class MessageResponseDto {
        private Long messageId;
        private Long senderId;
        private String senderName;
        private Long receiverId;
        private String receiverName;
        private String content;
        private Boolean isRead;
        private LocalDateTime createdAt;
    }
}
