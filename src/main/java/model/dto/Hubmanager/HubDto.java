package model.dto.Hubmanager;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

public class HubDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubCreateDto {
        private String name;
        private String address;
        private String city;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubResponseDto {
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubUpdateDto {
        private String name;
        private String address;
        private String city;
        private Integer numberOfRoutes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubStatsDto {
        private Long hubId;
        private String hubName;
        private String city;
        private Long totalAgents;
        private Long activeAgents;
        private Long totalDeliveries;
        private Long todayDeliveries;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubPerformanceDto {
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubManagerResponseDto {
        private Long hubManagerId;
        private Long hubId;
        private String hubName;
        private Long userId;
        private String userName;
        private String userEmail;
        private LocalDateTime joinedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignManagerDto {
        private Long userId;
    }
}
