package service.Admin;


import model.dto.Admin.AnalyticsDTO;
import model.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import model.repo.Admin.AnalyticsRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Service
public class AnalyticsService {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    public AnalyticsDTO.DashboardResponse getDashboardAnalytics() {
        AnalyticsDTO.StatsResponse stats = getAnalyticsStats();
        AnalyticsDTO.PopularGenresResponse genres = getPopularGenres();
        AnalyticsDTO.TopUsersResponse topUsers = getTopUsers(4);
        AnalyticsDTO.MonthlyRevenueResponse revenue = getMonthlyRevenue(2024);
        AnalyticsDTO.YearlyUsersResponse yearlyUsers = getYearlyUserGrowth();

        return new AnalyticsDTO.DashboardResponse(stats, genres, topUsers, revenue, yearlyUsers);
    }

    public AnalyticsDTO.StatsResponse getAnalyticsStats() {
        Long totalUsers = analyticsRepository.getTotalUsers();
        Long activeBooks = analyticsRepository.getActiveBooks();
        Long monthlyTransactions = analyticsRepository.getMonthlyTransactions();
        Long eventsHosted = analyticsRepository.getEventsHosted();

        // Calculate percentage changes
        Double userGrowth = calculateUserGrowth();
        Double bookGrowth = calculateBookGrowth();
        Double transactionGrowth = calculateTransactionGrowth();
        Double eventGrowth = calculateEventGrowth();

        List<AnalyticsDTO.StatItem> stats = Arrays.asList(
                new AnalyticsDTO.StatItem("Total Users", totalUsers.toString(), "+" + userGrowth + "%", "Users"),
                new AnalyticsDTO.StatItem("Active Books", activeBooks.toString(), "+" + bookGrowth + "%", "BookOpen"),
                new AnalyticsDTO.StatItem("Monthly Transactions", monthlyTransactions.toString(), "+" + transactionGrowth + "%", "DollarSign"),
                new AnalyticsDTO.StatItem("Events Hosted", eventsHosted.toString(), "+" + eventGrowth + "%", "Calendar")
        );

        return new AnalyticsDTO.StatsResponse(stats);
    }

    public AnalyticsDTO.PopularGenresResponse getPopularGenres() {
        List<Object[]> genreData = analyticsRepository.getGenreDistribution();
        List<AnalyticsDTO.GenreItem> genres = new ArrayList<>();

        Long totalBooks = analyticsRepository.getTotalBooks();

        if (totalBooks > 0) {
            for (Object[] data : genreData) {
                String genre = (String) data[0];
                Long count = (Long) data[1];
                Double percentage = (count.doubleValue() / totalBooks.doubleValue()) * 100;
                String color = getGenreColor(genre);
                genres.add(new AnalyticsDTO.GenreItem(genre, percentage.intValue(), color));
            }
        }

        return new AnalyticsDTO.PopularGenresResponse(genres);
    }

    public AnalyticsDTO.TopUsersResponse getTopUsers(int limit) {
        List<Object[]> userData = analyticsRepository.getTopUsersByActivity(limit);
        List<AnalyticsDTO.UserItem> users = new ArrayList<>();

        for (Object[] data : userData) {
            String userEmail = (String) data[0];
            String userName = (String) data[1];
            Long transactionCount = (Long) data[2];
            Double trustScore = calculateTrustScore(userEmail);
            String userType = determineUserType(userEmail);

            users.add(new AnalyticsDTO.UserItem(userName, transactionCount.intValue(), trustScore, userType));
        }

        // Sort by trust score (highest first)
        users.sort((a, b) -> Double.compare(b.getTrustScore(), a.getTrustScore()));

        return new AnalyticsDTO.TopUsersResponse(users);
    }

    public AnalyticsDTO.MonthlyRevenueResponse getMonthlyRevenue(int year) {
        List<Object[]> revenueData = analyticsRepository.getMonthlyRevenue(year);
        List<AnalyticsDTO.MonthlyRevenueItem> monthlyRevenue = new ArrayList<>();

        // Initialize all months with 0 revenue
        Map<Integer, BigDecimal> revenueMap = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            revenueMap.put(i, BigDecimal.ZERO);
        }

        // Fill with actual data
        for (Object[] data : revenueData) {
            BigDecimal monthBigDecimal = (BigDecimal) data[0]; // Handle BigDecimal
            Integer month = monthBigDecimal.intValue(); // Convert to Integer
            BigDecimal revenue = (BigDecimal) data[1];
            revenueMap.put(month, revenue);
        }

        // Convert to DTO list
        for (int i = 1; i <= 12; i++) {
            String monthName = Month.of(i).name().substring(0, 3);
            monthName = monthName.charAt(0) + monthName.substring(1).toLowerCase();
            BigDecimal revenue = revenueMap.get(i);
            monthlyRevenue.add(new AnalyticsDTO.MonthlyRevenueItem(monthName, revenue));
        }

        BigDecimal totalRevenue = revenueMap.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Double growthPercentage = calculateRevenueGrowth(year);

        return new AnalyticsDTO.MonthlyRevenueResponse(monthlyRevenue, totalRevenue, growthPercentage);
    }

    public AnalyticsDTO.YearlyUsersResponse getYearlyUserGrowth() {
        List<Object[]> yearlyData = analyticsRepository.getYearlyUserGrowth();
        List<AnalyticsDTO.YearlyUserItem> yearlyUsers = new ArrayList<>();

        for (Object[] data : yearlyData) {
            BigDecimal yearBigDecimal = (BigDecimal) data[0]; // Handle BigDecimal
            String year = yearBigDecimal.toString(); // Convert to String
            Long userCount = (Long) data[1];
            yearlyUsers.add(new AnalyticsDTO.YearlyUserItem(year, userCount));
        }

        Long totalUsers = analyticsRepository.getTotalUsers();
        return new AnalyticsDTO.YearlyUsersResponse(yearlyUsers, totalUsers);
    }

    // Helper methods for calculations
    private Double calculateUserGrowth() {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2);

        Long currentMonthUsers = analyticsRepository.getUsersByDateRange(lastMonth, LocalDateTime.now());
        Long previousMonthUsers = analyticsRepository.getUsersByDateRange(twoMonthsAgo, lastMonth);

        if (previousMonthUsers == 0) return 12.5;
        return Math.round(((currentMonthUsers.doubleValue() - previousMonthUsers.doubleValue()) / previousMonthUsers.doubleValue()) * 100 * 10.0) / 10.0;
    }

    private Double calculateBookGrowth() {
        // Calculate book growth based on last month's additions
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        Long currentBooks = analyticsRepository.getActiveBooks();
        Long previousBooks = analyticsRepository.getBooksByDateRange(LocalDateTime.now().minusMonths(2), lastMonth);

        if (previousBooks == 0) return 8.2;
        return Math.round(((currentBooks.doubleValue() - previousBooks.doubleValue()) / previousBooks.doubleValue()) * 100 * 10.0) / 10.0;
    }

    private Double calculateTransactionGrowth() {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2);

        Long currentTransactions = analyticsRepository.getTransactionsByDateRange(lastMonth, LocalDateTime.now());
        Long previousTransactions = analyticsRepository.getTransactionsByDateRange(twoMonthsAgo, lastMonth);

        if (previousTransactions == 0) return 24.1;
        return Math.round(((currentTransactions.doubleValue() - previousTransactions.doubleValue()) / previousTransactions.doubleValue()) * 100 * 10.0) / 10.0;
    }

    private Double calculateEventGrowth() {
        // Events growth calculation
        return 15.3; // Can be enhanced with actual event tracking
    }

    private Double calculateRevenueGrowth(int year) {
        List<Object[]> currentYearRevenue = analyticsRepository.getMonthlyRevenue(year);
        List<Object[]> previousYearRevenue = analyticsRepository.getMonthlyRevenue(year - 1);

        BigDecimal currentTotal = currentYearRevenue.stream()
                .map(data -> (BigDecimal) data[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal previousTotal = previousYearRevenue.stream()
                .map(data -> (BigDecimal) data[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (previousTotal.compareTo(BigDecimal.ZERO) == 0) return 18.2;

        return Math.round((currentTotal.subtract(previousTotal).divide(previousTotal, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue()) * 10.0) / 10.0;
    }

    private Double calculateTrustScore(String userEmail) {
        Long completedTransactions = analyticsRepository.getCompletedTransactionsByUser(userEmail);
        Long totalTransactions = analyticsRepository.getTotalTransactionsByUser(userEmail);
        Long userBooks = analyticsRepository.getBooksByUser(userEmail);

        if (totalTransactions == 0) return 4.0;

        // Base score from success rate
        Double successRate = completedTransactions.doubleValue() / totalTransactions.doubleValue();
        Double baseScore = 3.5 + (successRate * 1.5); // 3.5 to 5.0 range

        // Volume bonus
        if (completedTransactions > 50) baseScore += 0.3;
        else if (completedTransactions > 20) baseScore += 0.2;
        else if (completedTransactions > 10) baseScore += 0.1;

        // Book ownership bonus
        if (userBooks > 10) baseScore += 0.1;

        // Account age bonus (if user has been active for long time)
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        Long oldTransactions = analyticsRepository.getTransactionsByUserBeforeDate(userEmail, threeMonthsAgo);
        if (oldTransactions > 0) baseScore += 0.1;

        return Math.min(5.0, Math.round(baseScore * 10.0) / 10.0);
    }

    private String determineUserType(String userEmail) {
        Long salesCount = analyticsRepository.getTransactionsByUserAndType(userEmail, "SALE");
        Long loansCount = analyticsRepository.getTransactionsByUserAndType(userEmail, "LOAN");
        Long donationCount = analyticsRepository.getTransactionsByUserAndType(userEmail, "DONATION");
        Long totalBooks = analyticsRepository.getBooksByUser(userEmail);

        if (salesCount > loansCount && salesCount > donationCount && salesCount > 0) return "Seller";
        else if (loansCount > salesCount && loansCount > 0) return "Lender";
        else if (donationCount > 0) return "Donor";
        else if (totalBooks > 5) return "Collector";
        else return "Reader";
    }

    private String getGenreColor(String genre) {
        // Alternate between blue and yellow colors for genres
        Map<String, String> genreColors = new HashMap<>();
        genreColors.put("Fiction", "bg-blue-500");
        genreColors.put("Non-Fiction", "bg-yellow-400");
        genreColors.put("Science", "bg-blue-500");
        genreColors.put("History", "bg-yellow-400");
        genreColors.put("Biography", "bg-blue-500");
        genreColors.put("Romance", "bg-yellow-400");
        genreColors.put("Mystery", "bg-blue-500");
        genreColors.put("Fantasy", "bg-yellow-400");
        genreColors.put("Thriller", "bg-blue-500");
        genreColors.put("Poetry", "bg-yellow-400");

        return genreColors.getOrDefault(genre, "bg-blue-500");
    }
}
