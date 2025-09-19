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

@Entity
@Table(name = "bookstore_inventory")
@SQLRestriction("is_deleted <> true")
@Data @NoArgsConstructor @AllArgsConstructor
public class BSInventory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventoryId;

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
     * It is the name of the image file name only, stored in folder BSBook/coverImage/ */
    private String coverImage;

    @Enumerated(EnumType.STRING)
    private BookCondition condition;

    /** Actual description of book's content like a summary */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Just a short description to market the book whether selling or lending*/
    @Column(columnDefinition = "TEXT")
    private String terms;

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

    /** Total bulk copies */
    private Integer stockCount;

    /** How many of those are for SELL_ONLY */
    private Integer sellableCount;
    private Integer otherCount;

    private BigDecimal sellPrice;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private BookStore bookStore;

    /** If this is true, this whole inventory item is reserved for donations only
     * and CANNOT be brought into regular inventory again, */
    private Boolean isForDonation;

    private Boolean isDeleted = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        favouritesCount = 0;
        if (isDeleted == null) isDeleted = false;

        if (stockCount == null) stockCount = 0;
        if (sellableCount == null) sellableCount = 0;

        if (isForDonation == null) isForDonation = false;
        otherCount = stockCount - sellableCount;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        otherCount = stockCount - sellableCount;
    }

    public enum BookCondition {
        NEW, USED, FAIR
    }
}