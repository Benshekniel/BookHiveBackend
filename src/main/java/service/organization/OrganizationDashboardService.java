package service.organization;

import model.dto.organization.ActiveCompetitionDTO;
import model.dto.organization.DashboardStatsDTO;
import model.dto.organization.RecentRequestsDTO;
import model.dto.organization.TopDonorDTO;
import model.dto.organization.UpcomingEventsDTO;

import java.util.List;

public interface OrganizationDashboardService {

    DashboardStatsDTO getDashboardStats(Long organizationId);

    List<RecentRequestsDTO> getRecentRequests(Long organizationId);

    List<UpcomingEventsDTO> getUpcomingEvents(Long organizationId);

    List<TopDonorDTO> getTopDonors(Long organizationId);

    List<ActiveCompetitionDTO> getActiveCompetitions();
}