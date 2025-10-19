package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_requests")
@Data
@NoArgsConstructor
public class BookRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(nullable = false)
    private String title;

    @Column
    private String subject;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String urgency = "medium"; // low, medium, high

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String status; // DRAFT, PENDING, APPROVED, REJECTED, FULFILLED, CANCELED

    @Column(nullable = false)
    private LocalDateTime dateRequested;

    @Column
    private LocalDateTime dateApproved;

    @Column
    private LocalDateTime dateFulfilled;

    @Column
    private String rejectionReason;

    @Column
    private Long donorId;
}