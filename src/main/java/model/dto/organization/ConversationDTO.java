package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConversationDTO {
    private Long id;
    private Long partnerId;
    private String name;
    private String role;
    private String lastMessage;
    private String timestamp;
    private int unread;
    private String status;
}