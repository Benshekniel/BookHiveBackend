package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")  // 🔥 Add explicit mapping
    private Long transactionId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "created_at")  // 🔥 FIX: Map to database column
    private LocalDateTime createdAt;

    @Column(name = "start_date")  // 🔥 Add explicit mapping
    private LocalDateTime startDate;

    @Column(name = "end_date")    // 🔥 Add explicit mapping
    private LocalDateTime endDate;

    @Column(name = "return_date") // 🔥 Add explicit mapping
    private LocalDateTime returnDate;

    @Column(name = "payment_amount", precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "updated_at")  // 🔥 Add explicit mapping
    private LocalDateTime updatedAt;

    private String paMethodNew;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    /** book belonging to a customer type regular user */
    @Column(name = "book_id")
    private Long bookId;

    /** individual book items belonging to a bookstore user */
    private Integer bsBookId;

    /** bulk book items belonging to a bookstore user */
    private Integer bsInventoryId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "borrower_id")
    private Long borrowerId;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "lender_id")
    private Long lenderId;

    @Column(name = "exchanger_id")
    private Long exchangerId;

    @Column(name = "tracking_number", length = 50)
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method")
    private DeliveryMethod deliveryMethod;

    @Column(name = "delivery_address", columnDefinition = "TEXT")
    private String deliveryAddress;

    @Column(name = "estimated_delivery")
    private LocalDateTime estimatedDelivery;

    @Column(name = "actual_delivery")
    private LocalDateTime actualDelivery;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "borrow_period")
    private Integer borrowPeriod;

    @Column(name = "security_deposit", precision = 10, scale = 2)
    private BigDecimal securityDeposit;

    @Column(name = "overdue_days")
    private Integer overdueDays = 0;

    @Column(name = "exchange_period")
    private Integer exchangePeriod;

    @Column(name = "winning_bid", precision = 10, scale = 2)
    private BigDecimal winningBid;

    @Column(name = "auction_end_date")
    private LocalDateTime auctionEndDate;

    @Column(name = "delivery_amount", precision = 10, scale = 2)
    private BigDecimal deliveryAmount;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "deduction_amount", precision = 10, scale = 2)
    private BigDecimal deductionAmount;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (deliveryMethod == null) {
            deliveryMethod = DeliveryMethod.DELIVERY;
        }
        if (paymentMethod == null) {
            paymentMethod = PaymentMethod.CASH_ON_DELIVERY;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum TransactionType {
        SALE, LEND, EXCHANGE, BIDDING, AUCTION, DONATION
    }

    public enum TransactionStatus {
        ACTIVE, PENDING, COMPLETED, CANCELLED, OVERDUE
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }

    public enum DeliveryMethod {
        DELIVERY, PICKUP
    }

    public enum PaymentMethod {
        CASH_ON_DELIVERY, CREDIT_CARD, DEBIT_CARD, ONLINE, BANK_TRANSFER, WALLET
    }
}