// HubDTO.java - Complete Fixed Version
package model.dto.Delivery;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class HubDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubCreateDto {
        @NotBlank(message = "Hub name is required")
        @Size(min = 3, max = 100, message = "Hub name must be between 3 and 100 characters")
        private String name;

        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address cannot exceed 255 characters")
        private String address;

        @NotBlank(message = "City is required")
        @Size(max = 50, message = "City name cannot exceed 50 characters")
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
        @Size(min = 3, max = 100, message = "Hub name must be between 3 and 100 characters")
        private String name;

        @Size(max = 255, message = "Address cannot exceed 255 characters")
        private String address;

        @Size(max = 50, message = "City name cannot exceed 50 characters")
        private String city;

        @Min(value = 0, message = "Number of routes cannot be negative")
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
        @NotNull(message = "User ID is required")
        private Long userId;
    }
}