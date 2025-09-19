// BookRequestDto.java
package model.dto.Organization;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public class BookRequestDto {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookRequestResponseDto {
        private Long id;
        private Long organizationId;
        private String organizationName;
        private String title;
        private String description;
        private String status;
        private Integer quantity;
        private List<String> categories;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime fulfilledAt;
        private String priority;
        private String notes;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookRequestCreateDto {
        @NotNull(message = "Organization ID is required")
        private Long organizationId;
        
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        private String title;
        
        @Size(max = 500, message = "Description must be less than 500 characters")
        private String description;
        
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
        
        private List<String> categories;
        
        private String priority;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookRequestUpdateDto {
        private Long organizationId;
        
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        private String title;
        
        @Size(max = 500, message = "Description must be less than 500 characters")
        private String description;
        
        private String status;
        
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
        
        private List<String> categories;
        
        private String notes;
    }
}