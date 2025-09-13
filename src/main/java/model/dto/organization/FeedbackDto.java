// FeedbackDto.java
package model.dto.Organization;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class FeedbackDto {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedbackResponseDto {
        private Long id;
        private Long organizationId;
        private String organizationName;
        private Long donationId;
        private String donationTitle;
        private Integer rating;
        private String comment;
        private LocalDateTime createdAt;
        private String donorName;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedbackCreateDto {
        @NotNull(message = "Organization ID is required")
        private Long organizationId;
        
        private Long donationId;
        
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must be at most 5")
        private Integer rating;
        
        @Size(max = 500, message = "Comment must be less than 500 characters")
        private String comment;
    }
}