package model.dto.hubmanager;

import lombok.Data;
import java.time.LocalDateTime;


public class HubDto {
    @Data
    public class HubCreateDto {
        private String name;
        private String address;
        private String city;
    }

    @Data
    public class HubUpdateDto {
        private String name;
        private String address;
        private String city;
        private Integer numberOfRoutes;
    }

    @Data
    public class HubResponseDto {
        private Long hubId;
        private String name;
        private String address;
        private String city;
        private Integer numberOfRoutes;
        private LocalDateTime createdAt;
        private Long hubManagerId;
        private String hubManagerName;
        private Long totalAgents;
        private Long activeAgents;
        private Long totalDeliveries;
    }

    @Data
    public class HubStatsDto {
        private Long hubId;
        private String hubName;
        private String city;
        private Long totalAgents;
        private Long activeAgents;
        private Long totalDeliveries;
        private Long todayDeliveries;
    }

    @Data
    public class HubPerformanceDto {
        private Long hubId;
        private String hubName;
        private Long totalDeliveries;
        private Long successfulDeliveries;
        private Long failedDeliveries;
        private Double successRate;
        private Double avgDeliveryTime;
        private Double efficiencyScore;
    }

    @Data
    public class HubManagerResponseDto {
        private Long hubManagerId;
        private Long hubId;
        private String hubName;
        private Long userId;
        private String userName;
        private String userEmail;
        private LocalDateTime joinedAt;
    }

    @Data
    public class AssignManagerDto {
        private Long userId;
    }
}
