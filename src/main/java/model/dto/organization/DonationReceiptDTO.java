package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DonationReceiptDTO {
    @NotNull(message = "Organization ID is required")
    private Long organizationId;
    
    @NotBlank(message = "Received date is required")
    private String receivedDate;
    
    @NotBlank(message = "Condition is required")
    private String condition;
    
    private String notes;
}