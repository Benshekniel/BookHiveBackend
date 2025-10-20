package model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

public class UserTransactionDto {

    @Data
    public static class TransactionCreateDto {
        private Long bookId;
        private Long borrowerId;
        private Long sellerId;
        private Long lenderId;
        private Long exchangerId;
        private String type;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private BigDecimal paymentAmount;
        private String deliveryMethod;
        private String deliveryAddress;
        private LocalDateTime estimatedDelivery;
        private String paymentMethod;
        private Integer borrowPeriod;
        private BigDecimal securityDeposit;
        private Integer exchangePeriod;
        private BigDecimal winningBid;
        private LocalDateTime auctionEndDate;
        private BigDecimal deliveryAmount;
    }

    @Data
    public static class TransactionResponseDto {
        private Long transactionId;
        private String orderId; // Formatted as "ORD" + transactionId
        private String type;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime orderDate;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime returnDate;
        private LocalDateTime estimatedDelivery;
        private LocalDateTime actualDelivery;
        private BigDecimal paymentAmount;
        private BigDecimal totalAmount;
        private String paymentStatus;
        private String paymentMethod;

        // Book details
        private Long bookId;
        private String bookTitle;
        private String bookAuthor;
        private String bookCover;

        // User details (dynamic based on transaction type)
        private UserDetailsDto seller;
        private UserDetailsDto lender;
        private UserDetailsDto exchanger;
        private UserDetailsDto borrower;

        // Delivery details
        private String deliveryMethod;
        private String deliveryAddress;
        private String trackingNumber;

        // Type-specific fields
        private Integer borrowPeriod;
        private BigDecimal securityDeposit;
        private Integer overdueDays;
        private Integer exchangePeriod;
        private BigDecimal winningBid;
        private LocalDateTime auctionEndDate;
        private BigDecimal deliveryAmount;

        // Cancellation details
        private String cancelReason;
        private LocalDate cancelDate;
        private BigDecimal refundAmount;
        private BigDecimal deductionAmount;

        // Tracking information
        private List<TrackingDto> tracking;
    }

    @Data
    public static class UserDetailsDto {
        private Long userId;
        private String name;
        private String email;
        private String phone;
        private String location;
    }

    @Data
    public static class TrackingDto {
        private String status;
        private String timestamp;
        private String description;
    }

    @Data
    public static class TransactionStatsDto {
        private Long totalOrders;
        private Long activeOrders;
        private Long completedOrders;
        private Long overdueOrders;
        private Long cancelledOrders;
        private Long borrowedBooks;
        private Long purchasedBooks;
        private Long exchangedBooks;
        private Long wonAuctions;
        private BigDecimal totalSpent;
        private BigDecimal totalEarned;
    }

    @Data
    public static class TransactionFilterDto {
        private String status;
        private String type;
        private String paymentStatus;
        private LocalDateTime fromDate;
        private LocalDateTime toDate;
        private Long userId;
        private String sortBy = "createdAt";
        private String sortDirection = "DESC";
        private Integer page = 0;
        private Integer size = 20;
    }

    @Data
    public static class CancelOrderDto {
        private String reason;
        private String refundMethod;
        private String additionalNotes;
    }

    @Data
    public static class UpdateStatusDto {
        private String status;
        private String notes;
    }

    @Data
    public static class TrackingUpdateDto {
        private String status;
        private String description;
        private LocalDateTime timestamp;
    }
}