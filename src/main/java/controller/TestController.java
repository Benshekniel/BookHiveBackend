package controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:9999")
public class TestController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Backend is running");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organization/dashboard/{orgId}")
    public ResponseEntity<Map<String, Object>> getOrganizationDashboard(@PathVariable Long orgId) {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("orgId", orgId);
        dashboard.put("organizationName", "Test Organization");
        dashboard.put("organizationType", "Educational");
        
        Map<String, Integer> stats = new HashMap<>();
        stats.put("pendingRequests", 5);
        stats.put("booksReceived", 120);
        stats.put("upcomingEvents", 2);
        stats.put("totalDonations", 15);
        stats.put("activeRequests", 8);
        stats.put("deliveredDonations", 12);
        
        dashboard.put("stats", stats);
        return ResponseEntity.ok(dashboard);
    }

    @PostMapping("/organization/{orgId}/book-requests")
    public ResponseEntity<Map<String, String>> createBookRequest(
            @PathVariable Long orgId,
            @RequestBody Map<String, Object> requestData) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Book request created successfully");
        response.put("requestId", "REQ-" + System.currentTimeMillis());
        response.put("status", "PENDING");
        return ResponseEntity.ok(response);
    }
}
