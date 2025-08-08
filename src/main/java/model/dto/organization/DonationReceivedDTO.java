package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationReceivedDTO {
    private Long donationId;
    private String bookTitle;
    private String donorName;
    private String donorLocation;
    private Integer quantity;
    private String status; // "pending", "approved", "in_transit", "delivered", "received"
    private LocalDateTime dateReceived;
    private LocalDateTime dateShipped;
    private String trackingNumber;
    private String condition; // "excellent", "very_good", "good", "fair"
    private String notes;
    private LocalDateTime estimatedDelivery;
    private String bookCategory;
    private String gradeLevel;
}
