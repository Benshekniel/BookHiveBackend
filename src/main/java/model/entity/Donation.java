package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long donationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private Users donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_org_id")
    private Organization recipientOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status = DonationStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private BookCondition condition;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String trackingNumber;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime dateShipped;
    private LocalDateTime dateReceived;
    private LocalDateTime estimatedDelivery;

    private String donorLocation;
    private String bookCategory;
    private String gradeLevel;

    @Column(columnDefinition = "TEXT")
    private String recipientFeedback;

    private Integer actualQuantityReceived;
    private String receivedCondition;
    private boolean allItemsReceived = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum DonationStatus {
        PENDING, APPROVED, REJECTED, IN_TRANSIT, DELIVERED, RECEIVED, COMPLETED, CANCELLED
    }

    public enum BookCondition {
        EXCELLENT, VERY_GOOD, GOOD, FAIR, POOR
    }
        // Explicit getters and setters for all fields
        public Long getDonationId() { return donationId; }
        public void setDonationId(Long donationId) { this.donationId = donationId; }

        public Users getDonor() { return donor; }
        public void setDonor(Users donor) { this.donor = donor; }

        public Organization getRecipientOrganization() { return recipientOrganization; }
        public void setRecipientOrganization(Organization recipientOrganization) { this.recipientOrganization = recipientOrganization; }

        public Book getBook() { return book; }
        public void setBook(Book book) { this.book = book; }

        public String getBookTitle() { return bookTitle; }
        public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public DonationStatus getStatus() { return status; }
        public void setStatus(DonationStatus status) { this.status = status; }

        public BookCondition getCondition() { return condition; }
        public void setCondition(BookCondition condition) { this.condition = condition; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }

        public String getTrackingNumber() { return trackingNumber; }
        public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public LocalDateTime getDateShipped() { return dateShipped; }
        public void setDateShipped(LocalDateTime dateShipped) { this.dateShipped = dateShipped; }

        public LocalDateTime getDateReceived() { return dateReceived; }
        public void setDateReceived(LocalDateTime dateReceived) { this.dateReceived = dateReceived; }

        public LocalDateTime getEstimatedDelivery() { return estimatedDelivery; }
        public void setEstimatedDelivery(LocalDateTime estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }

        public String getDonorLocation() { return donorLocation; }
        public void setDonorLocation(String donorLocation) { this.donorLocation = donorLocation; }

        public String getBookCategory() { return bookCategory; }
        public void setBookCategory(String bookCategory) { this.bookCategory = bookCategory; }

        public String getGradeLevel() { return gradeLevel; }
        public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }

        public String getRecipientFeedback() { return recipientFeedback; }
        public void setRecipientFeedback(String recipientFeedback) { this.recipientFeedback = recipientFeedback; }

        public Integer getActualQuantityReceived() { return actualQuantityReceived; }
        public void setActualQuantityReceived(Integer actualQuantityReceived) { this.actualQuantityReceived = actualQuantityReceived; }

        public String getReceivedCondition() { return receivedCondition; }
        public void setReceivedCondition(String receivedCondition) { this.receivedCondition = receivedCondition; }

        public boolean isAllItemsReceived() { return allItemsReceived; }
        public void setAllItemsReceived(boolean allItemsReceived) { this.allItemsReceived = allItemsReceived; }
}