package model.dto.Admin;


import java.math.BigDecimal;
import java.util.List;

public class AnalyticsDTO {

    // Main Dashboard Response
    public static class DashboardResponse {
        private StatsResponse stats;
        private PopularGenresResponse popularGenres;
        private TopUsersResponse topUsers;
        private MonthlyRevenueResponse monthlyRevenue;
        private YearlyUsersResponse yearlyUsers;

        public DashboardResponse() {}

        public DashboardResponse(StatsResponse stats, PopularGenresResponse popularGenres,
                                 TopUsersResponse topUsers, MonthlyRevenueResponse monthlyRevenue,
                                 YearlyUsersResponse yearlyUsers) {
            this.stats = stats;
            this.popularGenres = popularGenres;
            this.topUsers = topUsers;
            this.monthlyRevenue = monthlyRevenue;
            this.yearlyUsers = yearlyUsers;
        }

        // Getters and Setters
        public StatsResponse getStats() { return stats; }
        public void setStats(StatsResponse stats) { this.stats = stats; }

        public PopularGenresResponse getPopularGenres() { return popularGenres; }
        public void setPopularGenres(PopularGenresResponse popularGenres) { this.popularGenres = popularGenres; }

        public TopUsersResponse getTopUsers() { return topUsers; }
        public void setTopUsers(TopUsersResponse topUsers) { this.topUsers = topUsers; }

        public MonthlyRevenueResponse getMonthlyRevenue() { return monthlyRevenue; }
        public void setMonthlyRevenue(MonthlyRevenueResponse monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; }

        public YearlyUsersResponse getYearlyUsers() { return yearlyUsers; }
        public void setYearlyUsers(YearlyUsersResponse yearlyUsers) { this.yearlyUsers = yearlyUsers; }
    }

    // Statistics Response
    public static class StatsResponse {
        private List<StatItem> stats;

        public StatsResponse() {}

        public StatsResponse(List<StatItem> stats) {
            this.stats = stats;
        }

        public List<StatItem> getStats() { return stats; }
        public void setStats(List<StatItem> stats) { this.stats = stats; }
    }

    public static class StatItem {
        private String label;
        private String value;
        private String change;
        private String icon;

        public StatItem() {}

        public StatItem(String label, String value, String change, String icon) {
            this.label = label;
            this.value = value;
            this.change = change;
            this.icon = icon;
        }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        public String getChange() { return change; }
        public void setChange(String change) { this.change = change; }

        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
    }

    // Popular Genres Response
    public static class PopularGenresResponse {
        private List<GenreItem> topGenres;

        public PopularGenresResponse() {}

        public PopularGenresResponse(List<GenreItem> topGenres) {
            this.topGenres = topGenres;
        }

        public List<GenreItem> getTopGenres() { return topGenres; }
        public void setTopGenres(List<GenreItem> topGenres) { this.topGenres = topGenres; }
    }

    public static class GenreItem {
        private String name;
        private Integer percentage;
        private String color;

        public GenreItem() {}

        public GenreItem(String name, Integer percentage, String color) {
            this.name = name;
            this.percentage = percentage;
            this.color = color;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getPercentage() { return percentage; }
        public void setPercentage(Integer percentage) { this.percentage = percentage; }

        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
    }

    // Top Users Response
    public static class TopUsersResponse {
        private List<UserItem> topUsers;

        public TopUsersResponse() {}

        public TopUsersResponse(List<UserItem> topUsers) {
            this.topUsers = topUsers;
        }

        public List<UserItem> getTopUsers() { return topUsers; }
        public void setTopUsers(List<UserItem> topUsers) { this.topUsers = topUsers; }
    }

    public static class UserItem {
        private String name;
        private Integer transactions;
        private Double trustScore;
        private String type;

        public UserItem() {}

        public UserItem(String name, Integer transactions, Double trustScore, String type) {
            this.name = name;
            this.transactions = transactions;
            this.trustScore = trustScore;
            this.type = type;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getTransactions() { return transactions; }
        public void setTransactions(Integer transactions) { this.transactions = transactions; }

        public Double getTrustScore() { return trustScore; }
        public void setTrustScore(Double trustScore) { this.trustScore = trustScore; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    // Monthly Revenue Response
    public static class MonthlyRevenueResponse {
        private List<MonthlyRevenueItem> monthlyRevenue;
        private BigDecimal totalRevenue;
        private Double growthPercentage;

        public MonthlyRevenueResponse() {}

        public MonthlyRevenueResponse(List<MonthlyRevenueItem> monthlyRevenue, BigDecimal totalRevenue, Double growthPercentage) {
            this.monthlyRevenue = monthlyRevenue;
            this.totalRevenue = totalRevenue;
            this.growthPercentage = growthPercentage;
        }

        public List<MonthlyRevenueItem> getMonthlyRevenue() { return monthlyRevenue; }
        public void setMonthlyRevenue(List<MonthlyRevenueItem> monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; }

        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

        public Double getGrowthPercentage() { return growthPercentage; }
        public void setGrowthPercentage(Double growthPercentage) { this.growthPercentage = growthPercentage; }
    }

    public static class MonthlyRevenueItem {
        private String month;
        private BigDecimal revenue;

        public MonthlyRevenueItem() {}

        public MonthlyRevenueItem(String month, BigDecimal revenue) {
            this.month = month;
            this.revenue = revenue;
        }

        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }

        public BigDecimal getRevenue() { return revenue; }
        public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
    }

    // Yearly Users Response
    public static class YearlyUsersResponse {
        private List<YearlyUserItem> yearlyUsers;
        private Long totalUsers;

        public YearlyUsersResponse() {}

        public YearlyUsersResponse(List<YearlyUserItem> yearlyUsers, Long totalUsers) {
            this.yearlyUsers = yearlyUsers;
            this.totalUsers = totalUsers;
        }

        public List<YearlyUserItem> getYearlyUsers() { return yearlyUsers; }
        public void setYearlyUsers(List<YearlyUserItem> yearlyUsers) { this.yearlyUsers = yearlyUsers; }

        public Long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }
    }

    public static class YearlyUserItem {
        private String year;
        private Long users;

        public YearlyUserItem() {}

        public YearlyUserItem(String year, Long users) {
            this.year = year;
            this.users = users;
        }

        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }

        public Long getUsers() { return users; }
        public void setUsers(Long users) { this.users = users; }
    }
}
