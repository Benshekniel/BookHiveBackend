package model.dto.Delivery;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import model.entity.Delivery;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class DeliveryDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryCreateDto {
        @NotBlank(message = "Pickup address is required")
        @Size(min = 10, max = 500, message = "Pickup address must be between 10 and 500 characters")
        private String pickupAddress;

        @NotBlank(message = "Delivery address is required")
        @Size(min = 10, max = 500, message = "Delivery address must be between 10 and 500 characters")
        private String deliveryAddress;

        @NotNull(message = "Hub ID is required")
        @Positive(message = "Hub ID must be positive")
        private Long hubId;

        private Long requesterId; // Optional

        @NotNull(message = "Transaction ID is required")
        @Positive(message = "Transaction ID must be positive")
        private Long transactionId;

        private Long agentId; // Optional for automatic assignment

        @NotNull(message = "User ID is required")
        @Positive(message = "User ID must be positive")
        private Long userId;

        // Additional optional fields for enhanced delivery creation
        private String weight;
        private String dimensions;
        private String deliveryNotes;
        private String paymentMethod;
        private String priority;
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
        private Long userId;
        private Long routeId; // Route assignment result
        private String routeName; // Route assignment result
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
        private Double deliveryLatitude; // Geocoded coordinates
        private Double deliveryLongitude; // Geocoded coordinates

        // Route assignment status
        private String routeAssignmentStatus; // SUCCESS, FAILED, NO_MATCH, GEOCODING_FAILED
        private String routeAssignmentMessage; // Additional info about assignment

        // Assignment method used
        private String assignmentMethod; // POLYGON_MATCH, POSTAL_CODE, DISTANCE, MANUAL
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
        @NotNull(message = "Status is required")
        private Delivery.DeliveryStatus status;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteAssignmentDto {
        private Long deliveryId;
        private String deliveryAddress;
        private Long currentRouteId;
        private Long suggestedRouteId;
        private String suggestedRouteName;
        private Double latitude;
        private Double longitude;
        private String assignmentStatus; // SUCCESS, FAILED, NO_MATCH
        private String errorMessage;
        private String assignmentMethod;
    }
}