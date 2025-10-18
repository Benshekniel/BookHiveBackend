//package model.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "bookstore_transactions")
//@Data @NoArgsConstructor @AllArgsConstructor
//public class BSTransaction {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long transactionId;
//
//    @ManyToOne
//    @JoinColumn(name = "store_id", nullable = false)
//    private BookStore bookStore;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private Users user;
//
//    @ManyToOne @JoinColumn(name = "inventory_id")
//    private BSInventory inventory;
//
//    @ManyToOne @JoinColumn(name = "book_id")
//    private BSBook book;
//
//    @Enumerated(EnumType.STRING)
//    private TransactionType transactionType;
//
//    private Integer quantity;
//    private BigDecimal price;
//    private LocalDateTime timestamp;
//
//    @PrePersist
//    protected void onCreate() {
//        timestamp = LocalDateTime.now();
//    }
//
//    public enum TransactionType {
//        SALE, LEND, DONATE, AUCTION,
//        RETURN
//    }
//}