// DashboardDto.java
package model.dto.Organization;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

public class DashboardDto {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardStatsDto {
        private Integer totalRequests;
        private Integer pendingRequests;
        private Integer completedRequests;
        private Integer totalDonations;
        private Double donationValue;
        private Integer unreadNotifications;
        private Double averageRating;
        private Integer activeRequests;
        private Integer rejectedRequests;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentRequestItem {
        private Long id;
        private String title;
        private String status;
        private LocalDateTime createdAt;
        private Integer quantity;
        private String category;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentRequestsResponseDto {
        private List<RecentRequestItem> requests;
        private Integer totalCount;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventItem {
        private Long id;
        private String title;
        private String description;
        private LocalDateTime eventDate;
        private String location;
        private String eventType;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpcomingEventsResponseDto {
        private List<EventItem> events;
        private Integer totalCount;
    }
}