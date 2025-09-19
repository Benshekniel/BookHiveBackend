package service.organization;

import model.dto.Organization.DashboardDto.*;
import java.util.Optional;

public interface DashboardService {
    
    // Get dashboard statistics
    Optional<DashboardStatsDto> getStats(Long orgId);
    
    // Get recent book requests
    Optional<RecentRequestsResponseDto> getRecentRequests(Long orgId);
    
    // Get upcoming events
    Optional<UpcomingEventsResponseDto> getUpcomingEvents(Long orgId);
}