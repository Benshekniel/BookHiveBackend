package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String businessRegistration;

    @Column(nullable = false)
    private String businessRegistrationCertificate;

    private String storeImageURL;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "jsonb")
    private String businessHours; // {"monday": "9:00-18:00", "tuesday": "9:00-18:00", ...}

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookType booksType; // "NEW_BOOKS, USED_BOOKS, BOTH"

    @Column(columnDefinition = "jsonb")
    private String approval;        // {"isApproved": yes/no/pending, "note": "approved by Moderator 12345"}

    @PrePersist
    protected void onCreate() {
        approval = "{isApproved: pending}";
    }

    // One-to-One relationship with AllUsers
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AllUsers user;

    public enum BookType {
        NEW_BOOKS, USED_BOOKS, BOTH
    }
}