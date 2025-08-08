package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long notificationId;
    private String title;
    private String message;
    private String type; // "info", "success", "warning", "error"
    private boolean isRead;
    private LocalDateTime createdDate;
    private String actionUrl;
    private Long relatedEntityId;
    private String relatedEntityType; // "donation", "request", "message"
}
