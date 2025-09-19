package service.organization;

import model.dto.Organization.DashboardDto.DashboardStatsDto;
import model.dto.Organization.DashboardDto.RecentRequestsResponseDto;
import model.dto.Organization.DashboardDto.UpcomingEventsResponseDto;

public interface OrganizationDashboardService {
    DashboardStatsDto getStats(Long orgId);
    RecentRequestsResponseDto getRecentRequests(Long orgId);
    UpcomingEventsResponseDto getUpcomingEvents(Long orgId);
}
