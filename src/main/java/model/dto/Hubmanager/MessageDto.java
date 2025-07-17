package model.dto.Hubmanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class MessageDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageCreateDto {
        private Long senderId;
        private Long receiverId;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponseDto {
        private Long messageId;
        private Long senderId;
        private String senderName;
        private Long receiverId;
        private String receiverName;
        private String content;
        private boolean isRead;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        
        public void setIsRead(boolean isRead) {
            this.isRead = isRead;
        }
    }
}
