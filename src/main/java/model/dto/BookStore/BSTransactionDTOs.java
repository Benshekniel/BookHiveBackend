//package model.dto.BookStore;
//
//import lombok.Data;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//public class BSTransactionDTOs {
//
//    @Data
//    public static class CreateRequest {
//        private Long bookId;
//        private Long inventoryId;
//        private String transactionType; // SALE, LEND, RETURN, etc.
//        private Integer quantity;
//        private BigDecimal price;
//    }
//
//    @Data
//    public static class Response {
//        private Long transactionId;
//        private Long bookId;
//        private Long inventoryId;
//        private String transactionType;
//        private Integer quantity;
//        private BigDecimal price;
//        private LocalDateTime timestamp;
//    }
//
//    @Data
//    public static class Summary {
//        private Long transactionId;
//        private String transactionType;
//        private BigDecimal price;
//        private LocalDateTime timestamp;
//    }
//}
//
