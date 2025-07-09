package model.dto.hubmanager;

import model.entity.agent.Agent;
import model.entity.delivery.Delivery;
import model.entity.AllUsers;
import lombok.Data;
import java.time.LocalDateTime;


public class DeliveryDto {
    @Data
    public class DeliveryCreateDto {
        private Long transactionId;
        private Long hubId;
        private Long agentId;
        private String pickupAddress;
        private String deliveryAddress;
    }

    @Data
    public class DeliveryResponseDto {
        private Long deliveryId;
        private Long transactionId;
        private Long hubId;
        private String hubName;
        private Long agentId;
        private String agentName;
        private String pickupAddress;
        private String deliveryAddress;
        private Delivery.DeliveryStatus status;
        private LocalDateTime pickupTime;
        private LocalDateTime deliveryTime;
        private String trackingNumber;
        private LocalDateTime createdAt;

        // Additional fields for frontend
        private String bookTitle;
        private String bookAuthor;
        private String customerName;
        private String customerPhone;
    }

    @Data
    public class DeliveryStatsDto {
        private Delivery.DeliveryStatus status;
        private Long count;
    }

    @Data
    public class UpdateStatusDto {
        private String status;
    }

    @Data
    public class AssignAgentDto {
        private Long agentId;
    }
}
