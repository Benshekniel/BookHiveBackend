package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DonationDTO {
    private Long id;
    private Long organizationId;
    private Long donorId;
    private String donorName; // Nullable, as not in Donation.java
    private String donorLocation; // Nullable, as not in Donation.java
    private String bookTitle;
    private Integer quantity;
    private Integer quantityCurrent; // Added to match Donation.java
    private String condition; // Nullable, as not in Donation.java
    private String status;
    private String trackingNumber;
    private String notes;
    private String donationDate;
    private String dateShipped;
    private String estimatedDelivery;
    private String dateReceived;
    private String priority; // Added to match Donation.java
}