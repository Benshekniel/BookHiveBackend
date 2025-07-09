package model.dto.hubmanager;

import model.entity.agent.Agent;
import model.entity.delivery.Delivery;
import model.entity.AllUsers;
import lombok.Data;
import java.time.LocalDateTime;


public class AgentDto {
    @Data
    public class AgentCreateDto {
        private Long userId;
        private Long hubId;
        private Agent.VehicleType vehicleType;
        private String vehicleNumber;
    }

    @Data
    public class AgentUpdateDto {
        private Agent.VehicleType vehicleType;
        private String vehicleNumber;
        private Agent.AvailabilityStatus availabilityStatus;
    }

    @Data
    public class AgentResponseDto {
        private Long agentId;
        private Long userId;
        private String userName;
        private String userEmail;
        private String userPhone;
        private Long hubId;
        private String hubName;
        private Agent.VehicleType vehicleType;
        private String vehicleNumber;
        private Agent.AvailabilityStatus availabilityStatus;
        private Double trustScore;
        private Integer deliveryTime;
        private Integer numberOfDelivery;
        private Double rating;
    }

    @Data
    public class AgentPerformanceDto {
        private Long agentId;
        private String name;
        private Integer deliveries;
        private Double successRate;
        private Integer avgTime;
        private Double rating;
    }

    @Data
    public class UpdateAgentStatusDto {
        private String status;
    }

    @Data
    public class UpdateDeliveryStatsDto {
        private Integer deliveryTime;
    }

    @Data
    public class UpdateTrustScoreDto {
        private Double trustScore;
    }

}
