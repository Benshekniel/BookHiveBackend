package model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
    private int user_id;


    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String fName;

    @Column(nullable = false)
    private String lName;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String businessRegistrationNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Uploaded image URLs
    private String storeImageName;

    // Uploaded Registraion image URLs
    private String registryImage;

    @Column(nullable = false, length = 10)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;     // first line of address: house no and street

    @Column(nullable = false)
    private String district; //state

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Integer esblishedYears;

    @Column(nullable = false, length = 5)
    private String postalCode; //zip

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb",nullable = true)
    private Map<String, String> businessHours; // {"monday": "9:00-18:00", "tuesday": "9:00-18:00", ...}

    @Column(nullable = true)
    private BookType booksType; // "NEW_BOOKS, USED_BOOKS, BOTH"

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // "PENDING, YES, NO"
  
    @Column(nullable = true)
    private String isApproved;    // "PENDING, YES, NO"

//    private String approvalNote;

    /** One-to-One relationship with AllUsers table */
//    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private AllUsers allUser;

//    /** Since AllUsers table user_id has a "_" in the middle we'll get it into a column by itself.
//     * So create new AllUsers objects and use that in service */
//    @Column(nullable = false)
//    private Integer userId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum BookType {
        NEW_BOOKS, USED_BOOKS, BOTH
    }

    public BookStore(String fName, String lName, String email, String phoneNumber, String address, String city, String district, String postalCode, String storeName, String businessRegistrationNumber, Integer esblishedYears) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.district = district;
        this.postalCode = postalCode;
        this.storeName = storeName;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.esblishedYears = esblishedYears;
    }

    public BookStore(String fName, String lName, String email, String phoneNumber, String address, String city, String district, String postalCode, String storeName, String businessRegistrationNumber, Integer esblishedYears, String registryImage, String isApproved) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.district = district;
        this.postalCode = postalCode;
        this.storeName = storeName;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.esblishedYears = esblishedYears;
        this.registryImage = registryImage;
        this.isApproved = isApproved;
    }
}