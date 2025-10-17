package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** Reviews for transactions involving books owned by BookStore users. */
@Entity
@Table(name = "bsbook_reviews")
@Data @NoArgsConstructor @AllArgsConstructor
public class BSBookReview {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne @JoinColumn(name = "book_id")
    private BSBook book;

    @ManyToOne @JoinColumn(name = "inventory_id")
    private BSInventory inventory;

    @ManyToOne @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

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
