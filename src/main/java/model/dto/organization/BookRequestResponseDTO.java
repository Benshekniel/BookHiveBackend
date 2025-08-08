package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestResponseDTO {
    private Long requestId;
    private String title;
    private String subject;
    private Integer quantity;
    private String description;
    private String status; // "pending", "approved", "rejected", "delivered", "cancelled"
    private String urgency;
    private String category;
    private String gradeLevel;
    private String notes;
    private LocalDateTime dateRequested;
    private LocalDateTime dateUpdated;
    private String approvedBy;
    private String rejectionReason;
    private String trackingNumber;
    private LocalDateTime estimatedDelivery;
}
