// AgentDTO.java - Complete Fixed Version
package model.dto.Delivery;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import model.entity.Agent;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class AgentDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentCreateDto {
        @NotNull(message = "Hub ID is required")
        private Long hubId;

        @NotNull(message = "User ID is required")
        private Long userId;

        private Agent.VehicleType vehicleType;

        @Size(max = 20, message = "Vehicle number cannot exceed 20 characters")
        private String vehicleNumber;

        @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
        private String phoneNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentResponseDto {
        private Long agentId;
        private Long id; // For frontend compatibility
        private Long hubId;
        private String hubName;
        private String hubCity;
        private Long userId;
        private String userName;
        private String name;
        private String userEmail;
        private String email;
        private String userPhone;
        private String phoneNumber;
        private Agent.VehicleType vehicleType;
        private String vehicleNumber;
        private Agent.AvailabilityStatus availabilityStatus;
        private Double trustScore;
        private Integer deliveryTime;
        private Integer numberOfDelivery;
        private Long totalDeliveries;
        private Double rating;
        private LocalDateTime createdAt;
        private String firstName;
        private String lastName;

        // Performance metrics
        private BigDecimal averageRating;
        private Integer completedDeliveries;
        private Integer onTimeDeliveries;
        private BigDecimal onTimePercentage;

        // Route-specific data
        private LocalDateTime assignedToRouteAt;
        private String routeAssignmentStatus;
        private Integer routeDeliveryCount;
        private String preferredRouteTypes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentPerformanceDto {
        private Long agentId;
        private String agentName;
        private String name;
        private Long totalDeliveries;
        private Long successfulDeliveries;
        private Long failedDeliveries;
        private Double successRate;
        private Double avgDeliveryTime;
        private Double trustScore;
        private Integer deliveries;
        private Integer avgTime;
        private Double rating;
        private Agent.AvailabilityStatus availabilityStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentUpdateDto {
        private String name;
        private String phone;
        private String address;
        private String nicPhoto;
        private String status;
        private String vehicleType;
        private String vehicleNumber;
        private String availabilityStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignAgentDto {
        @NotNull(message = "Agent ID is required")
        private Long agentId;

        @NotNull(message = "Delivery ID is required")
        private Long deliveryId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignAgentDeliveryDto {
        @NotNull(message = "Agent ID is required")
        private Long agentId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAgentStatusDto {
        @NotBlank(message = "Status is required")
        @Pattern(regexp = "AVAILABLE|BUSY|OFFLINE|ON_BREAK", message = "Invalid status")
        private String status;
    }
}