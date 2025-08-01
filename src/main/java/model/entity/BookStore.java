package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "bookstores")
@Data @NoArgsConstructor @AllArgsConstructor
public class BookStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeId;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String businessRegistrationNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String storeLogoImage;
    private String storeImage;

    private String email;
    private String phoneNumber;
    private String address;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> businessHours; // {"monday": "9:00-18:00", "tuesday": "9:00-18:00", ...}

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookType booksType; // "NEW_BOOKS, USED_BOOKS, BOTH"

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Approval isApproved;    // "PENDING, YES, NO"

    private String approvalNote;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        isApproved = Approval.PENDING;
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // One-to-One relationship with AllUsers
    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    public enum BookType {
        NEW_BOOKS, USED_BOOKS, BOTH
    }
    public enum Approval {
        PENDING, YES, NO
    }
}