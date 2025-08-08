package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long messageId;
    private String senderName;
    private String senderEmail;
    private String senderType; // "user", "admin", "moderator", "system"
    private String subject;
    private String content;
    private boolean isRead;
    private LocalDateTime sentDate;
    private String messageType; // "general", "donation", "request", "system"
    private Long relatedEntityId; // ID of related donation, request, etc.
}
