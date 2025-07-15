package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import model.entity.Agent;

public class AgentDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentCreateDto {
        private Long hubId;
        private Long userId;
        private Agent.VehicleType vehicleType;
        private String vehicleNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentResponseDto {
        private Long agentId;
        private Long hubId;
        private String hubName;
        private Long userId;
        private String userName;
        private String userEmail;
        private Agent.VehicleType vehicleType;
        private String vehicleNumber;
        private Agent.AvailabilityStatus availabilityStatus;
        private Double trustScore;
        private Integer deliveryTime;
        private Integer numberOfDelivery;
        private double rating;
        private String userPhone;
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
        private double rating;
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
