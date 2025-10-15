package model.dto.Delivery;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import model.entity.Agent;
import java.time.LocalDateTime;

public class AgentDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentCreateDto {
        private Long hubId;
        private Long userId;
        private Agent.VehicleType vehicleType;
        private String vehicleNumber;
        private String phoneNumber; // Added phone number field
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentResponseDto {
        private Long agentId;
        private Long id; // For frontend compatibility
        private Long hubId;
        private String hubName;
        private String hubCity; // Added hub city field
        private Long userId;
        private String userName;
        private String name; // Frontend expects this field
        private String userEmail;
        private String email; // Frontend expects this field
        private String userPhone;
        private String phoneNumber; // Changed to String for consistency
        private Agent.VehicleType vehicleType;
        private String vehicleNumber;
        private Agent.AvailabilityStatus availabilityStatus;
        private Double trustScore;
        private Integer deliveryTime;
        private Integer numberOfDelivery;
        private Long totalDeliveries; // Changed to Long for consistency
        private Double rating;
        private LocalDateTime createdAt; // Added created at field
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
        private Agent.AvailabilityStatus availabilityStatus; // Added availability status
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
        private Long agentId;
        private Long deliveryId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignAgentDeliveryDto {
        private Long agentId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAgentStatusDto {
        private String status;
    }
}