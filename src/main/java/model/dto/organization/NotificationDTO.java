// NotificationDto.java
package model.dto.Organization;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

public class NotificationDto {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationResponseDto {
        private Long id;
        private Long organizationId;
        private String type;
        private String message;
        private Boolean read;
        private LocalDateTime createdAt;
        private String title;
        private String action;
        private Long referenceId;
        private String referenceType;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarkAllReadResponseDto {
        private Integer count;
        private Boolean success;
        private String message;
    }
}