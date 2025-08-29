package model.dto.Delivery;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import model.entity.AllUsers;

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
        private Long receiverId;
        private String senderName;
        private String receiverName;
        private String content;
        private boolean Read; // Keeping your naming convention
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime sentAt;

        // Convenience methods for compatibility
        public boolean isRead() {
            return Read;
        }

        public void setRead(boolean read) {
            this.Read = read;
        }

        public void setIsRead(boolean read) {
            this.Read = read;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageUpdateDto {
        private String content;
        private boolean isRead;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationDto {
        private Long conversationId;
        private Long participantId;
        private String participantName;
        private String lastMessage;
        private LocalDateTime lastMessageTime;
        private int unreadCount;
        private boolean isOnline;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BroadcastMessageDto {
        private String message;
        private Long senderId;
        private Long hubId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageSummaryDto {
        private Long totalMessages;
        private Long totalConversations;
        private Long unreadCount;
        private LocalDateTime lastActivity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationSummaryDto {
        private Long partnerId;
        private String partnerName;
        private String lastMessage;
        private LocalDateTime lastMessageTime;
        private long unreadCount;
        private boolean isOnline;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageStatusDto {
        private Long messageId;
        private boolean isRead;
        private LocalDateTime readAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageSearchDto {
        private String searchTerm;
        private Long participantId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private boolean unreadOnly;
    }

    // Additional DTOs for enhanced messaging functionality
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactDto {
        private Integer userId;
        private String name;
        private String email;
        private String role;
        private AllUsers.Status status;
        private boolean isOnline;
        private LocalDateTime lastSeen;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationListDto {
        private Long partnerId;
        private String partnerName;
        private String partnerRole;
        private String lastMessage;
        private LocalDateTime lastMessageTime;
        private long unreadCount;
        private boolean isOnline;
        private String avatar; // For UI display
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageNotificationDto {
        private Long messageId;
        private Long senderId;
        private String senderName;
        private String content;
        private LocalDateTime sentAt;
        private boolean isRead;
    }
}