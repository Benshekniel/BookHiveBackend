package controller.Admin;

import model.dto.Admin.DashboardDTO;
import service.Admin.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:9999")
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Get complete dashboard data including metrics, recent activities, and quick action counts
     *
     * @return ResponseEntity containing dashboard data
     */
    @GetMapping
    public ResponseEntity<?> getDashboardData() {
        try {
            DashboardDTO dashboardData = dashboardService.getDashboardData();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Dashboard data retrieved successfully");
            response.put("data", dashboardData);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to retrieve dashboard data");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    /**
     * Get only dashboard metrics
     *
     * @return ResponseEntity containing metrics data
     */
    @GetMapping("/metrics")
    public ResponseEntity<?> getDashboardMetrics() {
        try {
            DashboardDTO dashboardData = dashboardService.getDashboardData();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Dashboard metrics retrieved successfully");
            response.put("data", dashboardData.getMetrics());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to retrieve dashboard metrics");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    /**
     * Get only recent activities
     *
     * @return ResponseEntity containing recent activities
     */
    @GetMapping("/activities")
    public ResponseEntity<?> getRecentActivities() {
        try {
            DashboardDTO dashboardData = dashboardService.getDashboardData();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Recent activities retrieved successfully");
            response.put("data", dashboardData.getRecentActivities());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to retrieve recent activities");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    /**
     * Get only quick action counts
     *
     * @return ResponseEntity containing quick action counts
     */
    @GetMapping("/quick-actions")
    public ResponseEntity<?> getQuickActionCounts() {
        try {
            DashboardDTO dashboardData = dashboardService.getDashboardData();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Quick action counts retrieved successfully");
            response.put("data", dashboardData.getQuickActionCounts());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to retrieve quick action counts");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    /**
     * Health check endpoint for dashboard service
     *
     * @return ResponseEntity with service status
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Dashboard service is running");
        response.put("service", "DashboardController");
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}
