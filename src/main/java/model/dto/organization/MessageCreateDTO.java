package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateDTO {
    private String recipientEmail;
    private String recipientType; // "user", "admin", "moderator"
    private String subject;
    private String content;
    private String messageType;
    private Long relatedEntityId;
}
