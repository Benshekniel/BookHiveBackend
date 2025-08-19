package controller.Admin;


import model.dto.Admin.AnalyticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.Admin.AnalyticsService;

@RestController
@CrossOrigin(origins = "http://localhost:9999")
@RequestMapping("/api/admin/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<AnalyticsDTO.DashboardResponse> getDashboardData() {
        AnalyticsDTO.DashboardResponse dashboard = analyticsService.getDashboardAnalytics();
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/stats")
    public ResponseEntity<AnalyticsDTO.StatsResponse> getStats() {
        AnalyticsDTO.StatsResponse stats = analyticsService.getAnalyticsStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/popular-genres")
    public ResponseEntity<AnalyticsDTO.PopularGenresResponse> getPopularGenres() {
        AnalyticsDTO.PopularGenresResponse genres = analyticsService.getPopularGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/top-users")
    public ResponseEntity<AnalyticsDTO.TopUsersResponse> getTopUsers(@RequestParam(defaultValue = "10") int limit) {
        AnalyticsDTO.TopUsersResponse topUsers = analyticsService.getTopUsers(limit);
        return ResponseEntity.ok(topUsers);
    }

    @GetMapping("/monthly-revenue/{year}")
    public ResponseEntity<AnalyticsDTO.MonthlyRevenueResponse> getMonthlyRevenue(@PathVariable int year) {
        AnalyticsDTO.MonthlyRevenueResponse revenue = analyticsService.getMonthlyRevenue(year);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/yearly-users")
    public ResponseEntity<AnalyticsDTO.YearlyUsersResponse> getYearlyUsers() {
        AnalyticsDTO.YearlyUsersResponse yearlyUsers = analyticsService.getYearlyUserGrowth();
        return ResponseEntity.ok(yearlyUsers);
    }
}
