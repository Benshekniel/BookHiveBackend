//package model.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//** Entity for books donated by BookStore users. */
//@Entity
//@Table(name = "bookstore_donations")
//@Data @NoArgsConstructor @AllArgsConstructor
//public class BSDonation {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer bsDonationId;
//
//    private Integer donatedBookCount;
//
//    @ManyToOne
//    @JoinColumn(name = "inventory_id", nullable = false)
//    private BSInventory bsInventory;
//
//    @ManyToOne
//    @JoinColumn(name = "store_id", nullable = false)
//    private BookStore bookStore;
//
//    /** fetch all the related data for donation from the donation table itself */
//    @ManyToOne
//    @JoinColumn(name = "donation_id", nullable = false)
//    private Donation donation;
//
//    private LocalDateTime createdAt;
//    private LocalDateTime completedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//    }
//}
