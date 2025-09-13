// Donation.java
package model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long organizationId;
    
    private String donorName;
    
    @Column(nullable = false)
    private String donationType;  // BOOK, MONEY, EQUIPMENT
    
    private Integer quantity;
    
    private Double value;
    
    @Column(nullable = false)
    private String status;  // PENDING, IN_TRANSIT, RECEIVED, CANCELLED
    
    @Column(nullable = false)
    private LocalDateTime donationDate;
    
    private LocalDateTime receivedDate;
    
    private String condition;  // GOOD, DAMAGED
    
    @Column(length = 1000)
    private String notes;
    
    private String bookTitle;
    
    private String bookAuthor;
    
    private String trackingNumber;
    
    @PrePersist
    protected void onCreate() {
        donationDate = LocalDateTime.now();
    }
}