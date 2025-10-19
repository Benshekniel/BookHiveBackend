package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor
@Entity @Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Just store the organization ID — not a foreign key object
    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(nullable = false)
    private Long donorId;

    @Column(nullable = false)
    private String bookTitle;

    @Column
    private Integer quantityCurrent;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String status; // PENDING, APPROVED, REJECTED, SHIPPED, IN_TRANSIT, RECEIVED

    @Column
    private String trackingNumber;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String category;

    @Column(nullable = true)
    private LocalDateTime dateDonated;

    @Column
    private LocalDate dateShipped;

    @Column
    private LocalDate estimatedDelivery;

    @Column
    private LocalDate dateReceived;

//    @OneToOne(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Feedback feedback;

    // 🔹 Priority column
    @Column(nullable = false)
    private String priority = "Medium"; // Default value

    // 🔹 Auto set creation timestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Column(nullable = true)
    private String rejectedReason;

    @Column(nullable = true)
    private String orgName;
}