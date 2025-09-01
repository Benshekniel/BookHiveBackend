package controller.organization;

import model.dto.organization.OrganizationDashboardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.organization.OrganizationDashboardService;
import java.util.List;

@RestController
@RequestMapping("/api/organization-dashboard")
public class OrganizationDashboardController {

    @Autowired
    private OrganizationDashboardService dashboardService;

    @GetMapping("/stats/{orgId}")
    public OrganizationDashboardDTO getStats(@PathVariable Long orgId) {
        return dashboardService.getStats(orgId);
    }

    @GetMapping("/recent-requests/{orgId}")
    public List<?> getRecentRequests(@PathVariable Long orgId) {
        return dashboardService.getRecentRequests(orgId);
    }

    @GetMapping("/upcoming-events/{orgId}")
    public List<?> getUpcomingEvents(@PathVariable Long orgId) {
        return dashboardService.getUpcomingEvents(orgId);
    }
}
