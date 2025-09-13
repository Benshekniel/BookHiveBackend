// OrganizationDto.java
package model.dto.Organization;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.validator.constraints.URL;

public class OrganizationDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrganizationResponseDto {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String address;
        private String description;
        private String imageUrl;
        private boolean twoFactorEnabled;
        private String websiteUrl;
        private String contactPerson;
        private String registrationNumber;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Integer totalBookRequests;
        private Integer totalDonations;
        private Double averageRating;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrganizationUpdateDto {
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        private String name;
        
        @Email(message = "Email must be valid")
        private String email;
        
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid")
        private String phone;
        
        @NotBlank(message = "Address is required")
        private String address;
        
        @Size(max = 500, message = "Description must be less than 500 characters")
        private String description;
        
        @URL(message = "Website URL must be valid")
        private String websiteUrl;
        
        private String contactPerson;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrganizationStatisticsDto {
        private Integer totalBookRequests;
        private Integer pendingRequests;
        private Integer completedRequests;
        private Integer totalDonationsReceived;
        private Double donationValue;
        private Double averageFeedbackRating;
        private Integer totalEvents;
        private Integer unreadNotifications;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageUploadResponseDto {
        private String imageUrl;
        private String message;
        private Boolean success;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordChangeRequestDto {
        @NotBlank(message = "Current password is required")
        private String currentPassword;
        
        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must be at least 8 characters")
        private String newPassword;
        
        @NotBlank(message = "Confirm password is required")
        private String confirmPassword;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordChangeResponseDto {
        private Boolean success;
        private String message;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TwoFactorAuthRequestDto {
        private Boolean enable;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TwoFactorAuthResponseDto {
        private Boolean enabled;
        private String message;
        private String setupKey;
    }
}