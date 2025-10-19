package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private Long organizationId;
    private String title;
    private String message;
    private String type;
    private boolean read;
    private String actionUrl;
    private String timestamp;
}