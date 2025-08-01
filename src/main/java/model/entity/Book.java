package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "books")
@Data @NoArgsConstructor @AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_Id")
    private Long bookId;

    @Column(nullable = false)
    private String title;

    // Things stored as arrays:
    @Type(StringArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> authors;

    @Type(StringArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> genres;

    @Type(StringArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> imageUrls;

    @Type(StringArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> tags;  // ["bestseller", "classic", "award-winner"]

    @Enumerated(EnumType.STRING)
    private BookCondition condition;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Enumerated(EnumType.STRING)
    private BookAvailability availability;

    // Enhanced listing types
    @Enumerated(EnumType.STRING)
    private ListingType listingType;

    // Pricing as JSON object
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, BigDecimal> pricing;
    // {"sellingPrice": 25.99, "lendingPrice": 5.00, "depositAmount": 15.00}

    @Column(columnDefinition = "TEXT")
    private String lendingTerms;

    // Essential book info
    private String isbn;
    private String publisher;
    private Integer publishedYear;
    private String language;
    private Integer pageCount;

    private Integer lendingPeriod;

    private Integer bookCount;  // mainly for bookstore when multiple books are from the same
    private Integer favouritesCount;

    // Series info as separate JSON object
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> seriesInfo;
    // {"series": "Harry Potter", "seriesNumber": 1, "totalBooks": 7}

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Foreign Keys
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
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

    public enum BookAvailability {
        AVAILABLE, UNAVAILABLE, RESERVED
    }

    public enum ListingType {
        SELL_ONLY, LEND_ONLY, EXCHANGE, DONATE, SELL_AND_LEND
    }
}