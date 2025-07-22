package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "books")
@Data @NoArgsConstructor @AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Column(nullable = false)
    private String title;

    // Arrays stored as JSON
    @Column(columnDefinition = "jsonb")
    @Convert(converter = StringListConverter.class)
    private List<String> authors;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = StringListConverter.class)
    private List<String> genres;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = StringListConverter.class)
    private List<String> imageUrls;

    @Enumerated(EnumType.STRING)
    private BookCondition condition;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Enumerated(EnumType.STRING)
    private ListingType listingType;

    // Pricing as JSON object
    @Column(columnDefinition = "jsonb")
    private String pricing; // {"sellingPrice": 25.99, "lendingPrice": 5.00, "depositAmount": 15.00}

    // Essential book info
    private String isbn;
    private String publisher;
    private Integer publishedYear;
    private String language;
    private Integer pageCount;

    private Integer lendingPeriod;

    private Integer bookCount;  // mainly for bookstore when multiple books are from the same
    private Integer favouritesCount;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = StringListConverter.class)
    private List<String> tags;  // ["bestseller", "classic", "award-winner"]

    // Series info as separate JSON object
    @Column(columnDefinition = "jsonb")
    private String seriesInfo;  // {"series": "Harry Potter", "seriesNumber": 1, "totalBooks": 7}

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Foreign Keys
//    @Column(name = "user_id", nullable = false)
//    private Long userId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        favouritesCount = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Enums
    public enum BookCondition {
        NEW, USED, FAIR
    }
    public enum BookStatus {
        UNAVAILABLE, AVAILABLE, SOLD, LENT, DONATED, AUCTION
    }
    public enum ListingType {
        SELL_ONLY, LEND_ONLY, EXCHANGE, DONATE, SELL_AND_LEND
    }
}