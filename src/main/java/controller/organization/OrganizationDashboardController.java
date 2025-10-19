



        package controller.organization;

import model.dto.organization.DashboardStatsDTO;
import model.dto.organization.RecentRequestsDTO;
import model.dto.organization.UpcomingEventsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.organization.OrganizationDashboardService;
import service.organization.impl.ResourceNotFoundException;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:9999", "http://localhost:3000", "http://localhost:9090"})
@RestController
@RequestMapping("/api/organization-dashboard")
public class OrganizationDashboardController {

    private final OrganizationDashboardService dashboardService;

    @Autowired
    public OrganizationDashboardController(OrganizationDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats/{organizationId}")
    public ResponseEntity<?> getDashboardStats(@PathVariable Long organizationId) {
        try {
            DashboardStatsDTO stats = dashboardService.getDashboardStats(organizationId);
            return ResponseEntity.ok(stats);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Failed to fetch dashboard stats: " + e.getMessage()));
        }
    }

    @GetMapping("/recent-requests/{organizationId}")
    public ResponseEntity<?> getRecentRequests(@PathVariable Long organizationId) {
        try {
            List<RecentRequestsDTO> requests = dashboardService.getRecentRequests(organizationId);
            return ResponseEntity.ok(requests);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Failed to fetch recent requests: " + e.getMessage()));
        }
    }

    @GetMapping("/upcoming-events/{organizationId}")
    public ResponseEntity<?> getUpcomingEvents(@PathVariable Long organizationId) {
        try {
            List<UpcomingEventsDTO> events = dashboardService.getUpcomingEvents(organizationId);
            return ResponseEntity.ok(events);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Failed to fetch upcoming events: " + e.getMessage()));
        }
    }

    // Error response class
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}//package controller.organization;
//
//import model.dto.organization.DashboardStatsDTO;
//import model.dto.organization.RecentRequestsDTO;
//import model.dto.organization.UpcomingEventsDTO;
//import service.organization.OrganizationDashboardService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//@CrossOrigin(origins = {"http://localhost:9999", "http://localhost:3000"})
//
//@RestController
//@RequestMapping("/api/organization-dashboard")
//public class OrganizationDashboardController {
//
//    private final OrganizationDashboardService dashboardService;
//
//    @Autowired
//    public OrganizationDashboardController(OrganizationDashboardService dashboardService) {
//        this.dashboardService = dashboardService;
//    }
//
//    @GetMapping("/stats/{organizationId}")
//    public ResponseEntity<DashboardStatsDTO> getDashboardStats(@PathVariable Long organizationId) {
//        DashboardStatsDTO stats = dashboardService.getDashboardStats(organizationId);
//        return ResponseEntity.ok(stats);
//    }
//
//    @GetMapping("/recent-requests/{organizationId}")
//    public ResponseEntity<List<RecentRequestsDTO>> getRecentRequests(@PathVariable Long organizationId) {
//        List<RecentRequestsDTO> requests = dashboardService.getRecentRequests(organizationId);
//        return ResponseEntity.ok(requests);
//    }
//
//    @GetMapping("/upcoming-events/{organizationId}")
//    public ResponseEntity<List<UpcomingEventsDTO>> getUpcomingEvents(@PathVariable Long organizationId) {
//        List<UpcomingEventsDTO> events = dashboardService.getUpcomingEvents(organizationId);
//        return ResponseEntity.ok(events);
//    }
//}