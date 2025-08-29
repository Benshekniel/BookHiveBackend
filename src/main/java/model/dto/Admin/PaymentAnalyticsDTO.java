package model.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAnalyticsDTO {
    private Long transactionId;
    private Transaction.TransactionType type;
    private Transaction.TransactionStatus status;
    private Transaction.PaymentStatus paymentStatus;
    private BigDecimal paymentAmount;
    private LocalDateTime createdAt;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long bookId;
    private Long userId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentStatsDTO {
        private BigDecimal totalRevenue;
        private Long totalTransactions;
        private Long pendingPayments;
        private BigDecimal refundedAmount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionFilterDTO {
        private String searchTerm;
        private Transaction.TransactionStatus status;
        private Transaction.TransactionType type;
        private Transaction.PaymentStatus paymentStatus;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private int page = 0;
        private int size = 10;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginatedResponseDTO<T> {
        private List<T> content;
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private int size;
        private boolean first;
        private boolean last;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionSummaryDTO {
        private String period;
        private Long transactionCount;
        private BigDecimal totalAmount;
        private BigDecimal averageAmount;
        private Long completedCount;
        private Long pendingCount;
        private Long failedCount;
        private Long refundedCount;
    }
}