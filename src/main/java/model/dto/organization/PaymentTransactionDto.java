package model.dto.organization;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentTransactionDto {

    @Data
    public static class TransactionCreateDto {
        private Long bookId;
        private Long borrowerId;
        private Long lenderId;
        private String type;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String paymentAmount;
    }

    @Data
    public static class TransactionResponseDto {
        private Long transactionId;
        private Long bookId;
        private String bookTitle;
        private Long borrowerId;
        private String borrowerName;
        private Long lenderId;
        private String lenderName;
        private String type;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime returnDate;
        private String paymentAmount;
        private String paymentStatus;
    }

    @Data
    public static class HubRevenueDto {
        private Long hubId;
        private String hubName;
        private BigDecimal totalRevenue;
        private Long totalTransactions;
        private Long completedTransactions;
        private Long pendingTransactions;
    }

    @Data
    public static class RevenueSummaryDto {
        private BigDecimal totalRevenue;
        private Long totalTransactions;
        private Long completedTransactions;
        private Long pendingTransactions;
        private Long failedTransactions;
    }

    @Data
    public static class TransactionStatsDto {
        private String status;
        private Long count;
        private BigDecimal totalAmount;
    }
}