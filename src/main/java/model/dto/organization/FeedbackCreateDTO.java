package model.dto.organization;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeedbackCreateDTO {
    @NotNull(message = "Organization ID is required")
    private Long organizationId;
    
    @NotNull(message = "Donation ID is required")
    private Long donationId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;
    
    @NotBlank(message = "Comment is required")
    @Size(min = 10, message = "Comment must be at least 10 characters")
    private String comment;
}