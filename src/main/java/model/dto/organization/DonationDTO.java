package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DonationDTO {
    private Long id;
    private Long organizationId;
    private Long donorId;
    private String donorName;
    private String donorLocation;
    private String bookTitle;
    private Integer quantity;
    private String condition;
    private String status;
    private String trackingNumber;
    private String notes;
    private String donationDate;
    private String dateShipped;
    private String estimatedDelivery;
    private String dateReceived;
}