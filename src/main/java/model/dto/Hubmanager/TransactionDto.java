package model.dto.Hubmanager;

import lombok.Data;
import java.time.LocalDateTime;

public class TransactionDto {
    @Data
    public class TransactionCreateDto {
        private Long bookId;
        private Long borrowerId;
        private Long lenderId;
        private String type;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String paymentAmount;
    }

    @Data
    public class TransactionResponseDto {
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
}
