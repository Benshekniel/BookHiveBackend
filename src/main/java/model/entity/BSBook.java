package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Entity for individual books owned by BookStores. */
@Entity
@Table(name = "bookstore_books")
@SQLRestriction("is_deleted <> true")
@Data @NoArgsConstructor @AllArgsConstructor
public class BSBook {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> tags;          // eg: ["bestseller", "classic", "award-winner"]

    /** Single image - cover page of book, preferably png from the internet itself.
     * It is the name of the image file name only, stored in folder BSItem/coverImage/ */
    private String coverImage;

    /** Multiple images, max 3 - photos of the book to showcase its condition
     * under folder BSItem/images/ */
    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> images;

    @Enumerated(EnumType.STRING)
    private BookCondition condition;

    /** Actual description of book's content like a summary */
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    private Boolean isForSelling;
    private BigDecimal sellPrice;

    private BigDecimal lendFee;

    /** Just a short description to market the book whether selling or lending*/
    @Column(columnDefinition = "TEXT")
    private String terms;

    private Integer circulations;
    private Integer lendingPeriod;
    private BigDecimal lateFee;
    private BigDecimal minTrustScore;

    // Essential book info:
    private String isbn;
    private String publisher;
    private Integer publishedYear;
    private String language;
    private Integer pageCount;

    private Integer favouritesCount;    // how many people have marked this book as favourite

    private String seriesName;
    private Integer seriesInstallment;
    private Integer seriesTotalBooks;

    // Timestamps:
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Boolean flags:
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private BookStore bookStore;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = BookStatus.AVAILABLE;
        favouritesCount = 0;
        circulations = 0;
        if (isDeleted == null) isDeleted = false;
        if (isForSelling == null) isForSelling = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum BookCondition {
        NEW, USED, FAIR
    }

    /** AVAILABLE, SOLD, LENT */
    public enum BookStatus {
        AVAILABLE, SOLD, LENT
    }
}