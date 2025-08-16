package model.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private DashboardMetrics metrics;
    private List<RecentActivity> recentActivities;
    private QuickActionCounts quickActionCounts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardMetrics {
        private Long totalUsers;
        private String totalUsersChange;
        private Long activeListings;
        private String activeListingsChange;
        private Long pendingApprovals;
        private String pendingApprovalsChange;
        private BigDecimal monthlyRevenue;
        private String monthlyRevenueChange;
        private Long activeEvents;
        private String activeEventsChange;
        private Double avgTrustScore;
        private String avgTrustScoreChange;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentActivity {
        private Long id;
        private String type;
        private String message;
        private String time;
        private String icon;
        private String color;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuickActionCounts {
        private Long pendingUserApprovals;
        private Long reportedItems;
        private Long activeDisputes;
        private Long activeModerators;
        private Long totalEvents;
    }
}
