package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/** Entity for books owned by BookStore users. */
@Entity
@Table(name = "bookstore_books")
@Data @NoArgsConstructor @AllArgsConstructor
public class BSBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Column(nullable = false)
    private String title;

    // Things stored as arrays:
    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> authors;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> genres;

    /** Single image - cover page of book, preferably png from the internet itself */
    private String coverImage;

    /** Multiple images, max 3 - photos of the book to showcase its condition */
    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> images;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> tags;          // ["bestseller", "classic", "award-winner"]

    @Enumerated(EnumType.STRING)
    private BookCondition condition;

    /** Actual description of book's content like a summary */
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Enumerated(EnumType.STRING)
    private ListingType listingType;

    /** Pricing as JSON object Containing: sellingPrice, lendingPrice */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, BigDecimal> pricing;
    // {"sellPrice": 25.99, "lendPrice": 5.00}

    /** Just a short description to market the book */
    @Column(columnDefinition = "TEXT")
    private String terms;

    // Essential book info:
    @Column(length = 13)
    private String isbn;

    private String publisher;
    private Integer publishedYear;
    private String language;
    private Integer pageCount;

    private Integer lendingPeriod;

    private Integer bookCount;          // mainly for bookstore when multiple books are from the same bookstore
    private Integer favouritesCount;    // how many people have marked this book as favourite

    /** Series info as a separate JSON object Containing: series, seriesNumber, totalBooks */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> seriesInfo;
    // {"series": "Harry Potter", "seriesNumber": 1, "totalBooks": 7}

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** Once again, connecting the bookStore fk properly */
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private BookStore bookStore;

//    /** StoreId integer foreign key connecting 'BookStores' table storeId */
//    @Column(nullable = false)
//    private Integer storeId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum BookCondition {
        NEW, USED, FAIR
    }
    /** UNAVAILABLE, AVAILABLE, SOLD, LENT, DONATED, AUCTION */
    public enum BookStatus {
        AVAILABLE,
        UNAVAILABLE, SOLD, LENT, AUCTION
        }
//        DONATED,

    /** SELL_ONLY, LEND_ONLY, SELL_AND_LEND */
    public enum ListingType {
        SELL_ONLY,
        LEND_ONLY,
        SELL_AND_LEND,
    }
//        EXCHANGE,
//        DONATE
}