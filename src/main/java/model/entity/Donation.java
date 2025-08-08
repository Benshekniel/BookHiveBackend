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
}