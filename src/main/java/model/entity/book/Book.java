package model.entity.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String genre;

    @Enumerated(EnumType.STRING)
    private BookCondition condition;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    private LocalDateTime createdAt;

    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String lendingTerms;

    @Enumerated(EnumType.STRING)
    private BookAvailability availability;

    // Foreign Keys
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum BookCondition {
        NEW, EXCELLENT, GOOD, FAIR, POOR
    }

    public enum BookStatus {
        AVAILABLE, SOLD, LENT, DONATED, AUCTION
    }

    public enum BookAvailability {
        AVAILABLE, UNAVAILABLE, RESERVED
    }
}