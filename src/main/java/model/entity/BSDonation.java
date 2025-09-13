package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/** Entity for books owned by BookStore users. */
@Entity
@Table(name = "bookstore_donations")
@Data @NoArgsConstructor @AllArgsConstructor
public class BSDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer donationId;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private BookStore bookStore;

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = DonationStatus.PENDING;
    }

    public enum DonationStatus {
        PENDING, REJECTED, APPROVED, COMPLETED
    }

}
