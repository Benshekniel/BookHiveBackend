package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationConfirmationDTO {
    private String receivedCondition;
    private Integer actualQuantityReceived;
    private String notes;
    private LocalDateTime dateReceived;
    private boolean allItemsReceived;
    private String feedback;
}
