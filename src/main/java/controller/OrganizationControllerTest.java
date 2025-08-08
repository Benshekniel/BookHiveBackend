package controller;

import model.dto.organization.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/organization")
@CrossOrigin(origins = "http://localhost:9999")
public class OrganizationControllerTest {

    // Dashboard - Your organization module's main endpoint
    @GetMapping("/{orgId}/dashboard")
    public ResponseEntity<OrganizationDashboardDTO> getDashboard(@PathVariable Long orgId) {
        // Create mock dashboard data to verify your frontend integration works
        OrganizationDashboardDTO.DashboardStats stats = new OrganizationDashboardDTO.DashboardStats(
            5, 120, 2, 15, 8, 12  // pendingRequests, booksReceived, upcomingEvents, totalDonations, activeRequests, deliveredDonations
        );
        
        OrganizationDashboardDTO dashboard = new OrganizationDashboardDTO(
            orgId, "Test Organization", "Educational", stats
        );
        
        return ResponseEntity.ok(dashboard);
    }

    // Book Requests - Core functionality of your organization module
    @PostMapping("/{orgId}/book-requests")
    public ResponseEntity<String> createBookRequest(
            @PathVariable Long orgId,
            @RequestBody BookRequestCreateDTO requestDTO) {
        // This verifies your frontend can send book request data correctly
        System.out.println("Organization " + orgId + " created book request: " + requestDTO.getTitle());
        return ResponseEntity.ok("Book request created successfully for organization: " + orgId);
    }

    @GetMapping("/{orgId}/book-requests")
    public ResponseEntity<List<BookRequestResponseDTO>> getBookRequests(
            @PathVariable Long orgId,
            @RequestParam(defaultValue = "all") String status) {
        // Return empty list for now - this proves the endpoint structure is correct
        return ResponseEntity.ok(new ArrayList<>());
    }

    // Test endpoint to verify your organization module is working
    @GetMapping("/{orgId}/health")
    public ResponseEntity<String> healthCheck(@PathVariable Long orgId) {
        return ResponseEntity.ok("Organization module is working for org: " + orgId);
    }
}
