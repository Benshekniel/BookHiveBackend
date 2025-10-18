package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Just store the organization ID — not a foreign key object
    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(nullable = false)
    private Long donorId;

    @Column(nullable = false)
    private String bookTitle;

    @Column
    private Integer quantityCurrent;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String status; // PENDING, APPROVED, REJECTED, SHIPPED, IN_TRANSIT, RECEIVED

    @Column
    private String trackingNumber;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String category;

    @Column(nullable = true)
    private LocalDateTime dateDonated;

    @Column
    private LocalDate dateShipped;

    @Column
    private LocalDate estimatedDelivery;

    @Column
    private LocalDate dateReceived;

//    @OneToOne(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Feedback feedback;

    // 🔹 Priority column
    @Column(nullable = false)
    private String priority = "Medium"; // Default value

    // 🔹 Auto set creation timestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Column(nullable = true)
    private String rejectedReason;

    @Column(nullable = true)
    private String orgName;
}

//package model.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Data
//@NoArgsConstructor
//@Entity
//@Table(name = "donations")
//public class Donation {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "organization_id", nullable = false)
//    private Organization organization;
//
//    @Column(nullable = false)
//    private Long donorId;
//
////    @Column
////    private String donorName;
////
////    @Column
////    private String donorLocation;
//
//    @Column(nullable = false)
//    private String bookTitle;
//
//    @Column(nullable = true)
//    private Integer quantityCurrent;
//
//    @Column(nullable = false)
//    private Integer quantity;
////
////    @Column
////    private String condition; // EXCELLENT, VERY_GOOD, GOOD, FAIR
//
//    @Column(nullable = false)
//    private String status; // PENDING, APPROVED, REJECTED, SHIPPED, IN_TRANSIT, RECEIVED
//
//    @Column
//    private String trackingNumber;
//
//    @Column(columnDefinition = "TEXT")
//    private String notes;
//
//    @Column(nullable = false)
//    private LocalDateTime dateDonated;
//
//    @Column
//    private LocalDate dateShipped;
//
//    @Column
//    private LocalDate estimatedDelivery;
//
//    @Column
//    private LocalDate dateReceived;
//
//    @OneToOne(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Feedback feedback;
//
//    // 🔹 NEW COLUMN: Priority (High / Medium / Low)
//    @Column(nullable = false)
//    private String priority = "Medium"; // Default value
//
//    // 🔹 NEW COLUMN: Auto set creation timestamp
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    // Automatically set createdAt before saving
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = LocalDateTime.now();
//    }
//}


//package model.entity;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Table;
//
//@Data
//@NoArgsConstructor
//@Entity
//@Table(name = "donations")
//public class Donation {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "organization_id", nullable = false)
//    private Organization organization;
//
//    @Column(nullable = false)
//    private Long donorId;
//
//    @Column
//    private String donorName;
//
//    @Column
//    private String donorLocation;
//
//    @Column(nullable = false)
//    private String bookTitle;
//
//    @Column(nullable = false)
//    private Integer quantity;
//
//    @Column
//    private String condition; // EXCELLENT, VERY_GOOD, GOOD, FAIR
//
//    @Column(nullable = false)
//    private String status; // PENDING, APPROVED, REJECTED, SHIPPED, IN_TRANSIT, RECEIVED
//
//    @Column
//    private String trackingNumber;
//
//    @Column(columnDefinition = "TEXT")
//    private String notes;
//
//    @Column(nullable = false)
//    private LocalDateTime dateDonated;
//
//    @Column
//    private LocalDate dateShipped;
//
//    @Column
//    private LocalDate estimatedDelivery;
//
//    @Column
//    private LocalDate dateReceived;
//
//    @OneToOne(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Feedback feedback;
//}

//package model.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "donations")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Donation {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long donationId;
//
//    @Enumerated(EnumType.STRING)
//    private DonationStatus status;
//
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//    }
//
//    public enum DonationStatus {
//        PENDING, APPROVED, REJECTED, COMPLETED
//    }
//}