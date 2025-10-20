package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "reviewer_user_id", nullable = false)
    private Long reviewerUserId;

    @Column(name = "reviewed_user_id") // The user being reviewed (seller/lender/exchanger)
    private Long reviewedUserId;

    @Column(name = "seller_rating") // Rating for the seller/lender/exchanger
    private Integer sellerRating; // 1-5 stars

    @Column(name = "book_condition_rating") // Rating for book condition
    private Integer bookConditionRating; // 1-5 stars

    @Column(name = "overall_rating", nullable = false) // Overall experience rating
    private Integer overallRating; // 1-5 stars

    @Column(name = "review_comment", columnDefinition = "TEXT")
    private String reviewComment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}