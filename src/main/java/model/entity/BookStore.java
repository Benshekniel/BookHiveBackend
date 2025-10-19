package model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "bookstores")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column
    private String storeLogoImage;

    @Column
    private String storeImage;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 10)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 5)
    private String postalCode;

    @Column(nullable = false)
    private String district;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> businessHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookType booksType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Approval isApproved;

    @Column
    private String approvalNote;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AllUsers allUser;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        isApproved = Approval.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum BookType {
        NEW_BOOKS, USED_BOOKS, BOTH
    }

    public enum Approval {
        PENDING, YES, NO
    }
}