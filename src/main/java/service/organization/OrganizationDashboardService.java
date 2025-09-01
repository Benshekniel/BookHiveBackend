package service.organization;

import model.dto.organization.OrganizationDashboardDTO;
import java.util.List;

public interface OrganizationDashboardService {
    OrganizationDashboardDTO getStats(Long orgId);
    List<?> getRecentRequests(Long orgId);
    List<?> getUpcomingEvents(Long orgId);
}
