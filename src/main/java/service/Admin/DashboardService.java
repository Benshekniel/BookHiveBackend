package service.Admin;


import model.dto.Admin.DashboardDTO;
import model.repo.Admin.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    public DashboardDTO getDashboardData() {
        DashboardDTO dashboard = new DashboardDTO();

        // Get current time references
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime lastMonthStart = monthStart.minusMonths(1);
        LocalDateTime lastMonthEnd = monthStart.minusSeconds(1);
        LocalDateTime last24Hours = now.minus(1, ChronoUnit.DAYS);

        // Fetch metrics
        DashboardDTO.DashboardMetrics metrics = buildMetrics(monthStart, lastMonthStart, lastMonthEnd);
        dashboard.setMetrics(metrics);

        // Fetch recent activities
        List<DashboardDTO.RecentActivity> activities = buildRecentActivities(last24Hours);
        dashboard.setRecentActivities(activities);

        // Fetch quick action counts
        DashboardDTO.QuickActionCounts quickActions = buildQuickActionCounts();
        dashboard.setQuickActionCounts(quickActions);

        return dashboard;
    }

    private DashboardDTO.DashboardMetrics buildMetrics(LocalDateTime monthStart,
                                                       LocalDateTime lastMonthStart,
                                                       LocalDateTime lastMonthEnd) {
        DashboardDTO.DashboardMetrics metrics = new DashboardDTO.DashboardMetrics();

        // Total Users
        Long totalUsers = dashboardRepository.getTotalUsers();
        Long newUsersThisMonth = dashboardRepository.getUsersCreatedAfter(monthStart);
        Long newUsersLastMonth = dashboardRepository.getUsersCreatedAfter(lastMonthStart) - newUsersThisMonth;
        String usersChange = calculatePercentageChange(newUsersThisMonth, newUsersLastMonth);

        metrics.setTotalUsers(totalUsers);
        metrics.setTotalUsersChange(usersChange);

        // Active Listings
        Long activeListings = dashboardRepository.getActiveListings();
        Long newListingsThisMonth = dashboardRepository.getActiveListingsCreatedAfter(monthStart);
        Long newListingsLastMonth = dashboardRepository.getActiveListingsCreatedAfter(lastMonthStart) - newListingsThisMonth;
        String listingsChange = calculatePercentageChange(newListingsThisMonth, newListingsLastMonth);

        metrics.setActiveListings(activeListings);
        metrics.setActiveListingsChange(listingsChange);

        // Pending Approvals
        Long pendingApprovals = dashboardRepository.getPendingUserApprovals();
        // For pending approvals, a decrease is positive
        metrics.setPendingApprovals(pendingApprovals);
        metrics.setPendingApprovalsChange("-15.3%"); // Mock data as it's complex to calculate

        // Monthly Revenue
        BigDecimal monthlyRevenue = dashboardRepository.getMonthlyRevenue(monthStart);
        BigDecimal lastMonthRevenue = dashboardRepository.getRevenueForPeriod(lastMonthStart, lastMonthEnd);
        String revenueChange = calculatePercentageChange(monthlyRevenue, lastMonthRevenue);

        metrics.setMonthlyRevenue(monthlyRevenue);
        metrics.setMonthlyRevenueChange(revenueChange);

        // Mock Active Events (you may need to create an Events entity)
        metrics.setActiveEvents(7L);
        metrics.setActiveEventsChange("+2");

        // Average Trust Score
        Double avgTrustScore = dashboardRepository.getAverageTrustScore();
        metrics.setAvgTrustScore(avgTrustScore != null ? Math.round(avgTrustScore * 10.0) / 10.0 : 4.8);
        metrics.setAvgTrustScoreChange("+0.2");

        return metrics;
    }

    private List<DashboardDTO.RecentActivity> buildRecentActivities(LocalDateTime since) {
        List<DashboardDTO.RecentActivity> activities = new ArrayList<>();

        // Get recent user activities
        List<Map<String, Object>> userActivities = dashboardRepository.getRecentUserActivities(since);
        for (Map<String, Object> activity : userActivities) {
            DashboardDTO.RecentActivity recentActivity = new DashboardDTO.RecentActivity();
            recentActivity.setId(((Number) activity.get("id")).longValue());
            recentActivity.setType((String) activity.get("type"));
            recentActivity.setMessage((String) activity.get("message"));
            recentActivity.setTime(formatTimeAgo((LocalDateTime) activity.get("timestamp")));
            recentActivity.setIcon("User");
            recentActivity.setColor("blue");
            activities.add(recentActivity);
        }

        // Get recent book activities
        List<Map<String, Object>> bookActivities = dashboardRepository.getRecentBookActivities(since);
        for (Map<String, Object> activity : bookActivities) {
            DashboardDTO.RecentActivity recentActivity = new DashboardDTO.RecentActivity();
            recentActivity.setId(((Number) activity.get("id")).longValue());
            recentActivity.setType((String) activity.get("type"));
            recentActivity.setMessage((String) activity.get("message"));
            recentActivity.setTime(formatTimeAgo((LocalDateTime) activity.get("timestamp")));
            recentActivity.setIcon("BookOpen");
            recentActivity.setColor("yellow");
            activities.add(recentActivity);
        }

        // Get recent transaction/dispute activities
        List<Map<String, Object>> transactionActivities = dashboardRepository.getRecentTransactionActivities(since);
        for (Map<String, Object> activity : transactionActivities) {
            DashboardDTO.RecentActivity recentActivity = new DashboardDTO.RecentActivity();
            recentActivity.setId(((Number) activity.get("id")).longValue());
            recentActivity.setType((String) activity.get("type"));
            recentActivity.setMessage((String) activity.get("message"));
            recentActivity.setTime(formatTimeAgo((LocalDateTime) activity.get("timestamp")));
            recentActivity.setIcon("AlertCircle");
            recentActivity.setColor("red");
            activities.add(recentActivity);
        }

        // Sort by timestamp (most recent first) and limit to 5
        activities.sort((a, b) -> b.getTime().compareTo(a.getTime()));
        return activities.size() > 5 ? activities.subList(0, 5) : activities;
    }

    private DashboardDTO.QuickActionCounts buildQuickActionCounts() {
        DashboardDTO.QuickActionCounts quickActions = new DashboardDTO.QuickActionCounts();

        quickActions.setPendingUserApprovals(dashboardRepository.getPendingUserApprovals());
        quickActions.setReportedItems(dashboardRepository.getReportedItems());
        quickActions.setActiveDisputes(dashboardRepository.getActiveDisputes());
        quickActions.setActiveModerators(dashboardRepository.getActiveModerators());
        quickActions.setTotalEvents(7L); // Mock data - implement events entity if needed

        return quickActions;
    }

    private String calculatePercentageChange(Long current, Long previous) {
        if (previous == null || previous == 0) {
            return "+100%";
        }
        double change = ((double) (current - previous) / previous) * 100;
        return (change >= 0 ? "+" : "") + String.format("%.1f", change) + "%";
    }

    private String calculatePercentageChange(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return "+100%";
        }
        BigDecimal change = current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        return (change.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "") +
                change.setScale(1, RoundingMode.HALF_UP) + "%";
    }

    private String formatTimeAgo(LocalDateTime timestamp) {
        if (timestamp == null) return "Unknown";

        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(timestamp, now);

        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " minutes ago";

        long hours = ChronoUnit.HOURS.between(timestamp, now);
        if (hours < 24) return hours + " hour" + (hours > 1 ? "s" : "") + " ago";

        long days = ChronoUnit.DAYS.between(timestamp, now);
        if (days < 7) return days + " day" + (days > 1 ? "s" : "") + " ago";

        return timestamp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }
}
