package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopDonorDTO {
    private Long id;
    private Long donorId;
    private String donorName;
    private String name; // Alias for donorName for frontend compatibility
    private Integer totalBooks;
    private Integer quantity; // Alias for totalBooks for frontend compatibility
    private Integer donationCount;
    private LocalDateTime lastDonationDate;
    private LocalDateTime lastDonation; // Alias for lastDonationDate for frontend compatibility

    // Constructor for query projection
    public TopDonorDTO(Long donorId, String donorName, Long totalBooks, Long donationCount, LocalDateTime lastDonationDate) {
        this.donorId = donorId;
        this.donorName = donorName;
        this.name = donorName; // Set alias
        this.totalBooks = totalBooks != null ? totalBooks.intValue() : 0;
        this.quantity = this.totalBooks; // Set alias
        this.donationCount = donationCount != null ? donationCount.intValue() : 0;
        this.lastDonationDate = lastDonationDate;
        this.lastDonation = lastDonationDate; // Set alias
    }
}
