package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

// Add Jakarta validation imports
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class BookRequestCreateDTO {
    @NotNull(message = "Organization ID is required")
    private Long organizationId;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String subject;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    private String urgency = "medium";
    
    private String description;
}