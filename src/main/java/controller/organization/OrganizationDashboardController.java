// 2. OrganizationDashboardController.java
package controller.organization;

import model.dto.Organization.DashboardDto.DashboardStatsDto;
import model.dto.Organization.DashboardDto.RecentRequestsResponseDto;
import model.dto.Organization.DashboardDto.UpcomingEventsResponseDto;
import service.organization.OrganizationDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization-dashboard")
@RequiredArgsConstructor
public class OrganizationDashboardController {

    private final OrganizationDashboardService dashboardService;

    @GetMapping("/stats/{orgId}")
    public ResponseEntity<DashboardStatsDto> getStats(@PathVariable Long orgId) {
        DashboardStatsDto stats = dashboardService.getStats(orgId);
        if (stats != null) {
            return ResponseEntity.ok(stats);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/recent-requests/{orgId}")
    public ResponseEntity<RecentRequestsResponseDto> getRecentRequests(@PathVariable Long orgId) {
        RecentRequestsResponseDto requests = dashboardService.getRecentRequests(orgId);
        if (requests != null) {
            return ResponseEntity.ok(requests);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/upcoming-events/{orgId}")
    public ResponseEntity<UpcomingEventsResponseDto> getUpcomingEvents(@PathVariable Long orgId) {
        UpcomingEventsResponseDto events = dashboardService.getUpcomingEvents(orgId);
        if (events != null) {
            return ResponseEntity.ok(events);
        }
        return ResponseEntity.notFound().build();
    }
}