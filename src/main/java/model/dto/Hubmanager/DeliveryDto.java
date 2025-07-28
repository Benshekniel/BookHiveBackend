package model.dto.Hubmanager;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import model.entity.Delivery;
import java.time.LocalDateTime;

public class DeliveryDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryCreateDto {
        private String pickupAddress;
        private String deliveryAddress;
        private Long hubId;
        private Long requesterId;
        private Long transactionId;
        private Long agentId;
        private long userId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryResponseDto {
        private Long deliveryId;
        private String pickupAddress;
        private String deliveryAddress;
        private Delivery.DeliveryStatus status;
        private LocalDateTime pickupTime;
        private LocalDateTime deliveryTime;
        private String trackingNumber;
        private LocalDateTime createdAt;
        private Long hubId;
        private String hubName;
        private Long agentId;
        private long userId;
        private String agentName;
        private String customerName;
        private String customerPhone;
        private Long transactionId;
        private String bookTitle;
        private String bookAuthor;
        private String agentEmail;
        private String customerEmail;
        private String agentPhone;
        private String value;
        private String priority;
        private String description;
        private String weight;
        private String dimensions;
        private String paymentMethod;
        private String deliveryNotes;
        private LocalDateTime estimatedDelivery;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryStatsDto {
        private Long totalDeliveries;
        private Long pendingDeliveries;
        private Long inTransitDeliveries;
        private Long deliveredDeliveries;
        private Long failedDeliveries;
        private Double successRate;
        private Double avgDeliveryTime;
        private Delivery.DeliveryStatus status;
        private Long count;
        
        public DeliveryStatsDto(Delivery.DeliveryStatus status, long count) {
            this.status = status;
            this.count = count;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateStatusDto {
        private Delivery.DeliveryStatus status;
        private String notes;
    }
}
