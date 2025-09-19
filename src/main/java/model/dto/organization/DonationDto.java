// DonationDto.java
package model.dto.Organization;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class DonationDto {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DonationResponseDto {
        private Long id;
        private Long organizationId;
        private String organizationName;
        private String donorName;
        private String donationType;
        private Integer quantity;
        private Double value;
        private String status;
        private LocalDateTime donationDate;
        private LocalDateTime receivedDate;
        private String condition;
        private String notes;
        private String bookTitle;
        private String bookAuthor;
        private String trackingNumber;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DonationConfirmationDto {
        @NotNull(message = "Organization ID is required")
        private Long organizationId;
        
        private String notes;
        
        private Boolean isConditionSatisfactory;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PendingDonationDto {
        private Long id;
        private String donorName;
        private String donationType;
        private Integer quantity;
        private LocalDateTime donationDate;
        private String bookTitle;
        private String status;
        private Double value;
    }
}