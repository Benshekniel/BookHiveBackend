// 2. OrganizationDashboardController.java
package controller.organization;

import model.dto.Organization.DashboardDto.*;
import service.organization.impl.DashboardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization-dashboard")
@RequiredArgsConstructor
public class OrganizationDashboardController {

    private final DashboardServiceImpl dashboardService;

    @GetMapping("/stats/{orgId}")
    public ResponseEntity<DashboardStatsDto> getStats(@PathVariable Long orgId) {
        return dashboardService.getStats(orgId)
                .map(stats -> ResponseEntity.ok(stats))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/recent-requests/{orgId}")
    public ResponseEntity<RecentRequestsResponseDto> getRecentRequests(@PathVariable Long orgId) {
        return dashboardService.getRecentRequests(orgId)
                .map(requests -> ResponseEntity.ok(requests))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/upcoming-events/{orgId}")
    public ResponseEntity<UpcomingEventsResponseDto> getUpcomingEvents(@PathVariable Long orgId) {
        return dashboardService.getUpcomingEvents(orgId)
                .map(events -> ResponseEntity.ok(events))
                .orElse(ResponseEntity.notFound().build());
    }
}