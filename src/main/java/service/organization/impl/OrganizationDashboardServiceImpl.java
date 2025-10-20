

package service.organization.impl;

import model.dto.organization.ActiveCompetitionDTO;
import model.dto.organization.DashboardStatsDTO;
import model.dto.organization.RecentRequestsDTO;
import model.dto.organization.TopDonorDTO;
import model.dto.organization.UpcomingEventsDTO;
import model.entity.BookRequest;
import model.entity.Competitions;
import model.repo.CompetitionRepo;
import model.repo.OrgRepo;
import model.repo.organization.BookRequestRepository;
import model.repo.organization.DonationRepository;
import service.organization.OrganizationDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationDashboardServiceImpl implements OrganizationDashboardService {

    private final OrgRepo orgRepo;
    private final BookRequestRepository bookRequestRepository;
    private final DonationRepository donationRepository;
    private final CompetitionRepo competitionRepo;

    @Autowired
    public OrganizationDashboardServiceImpl(
            OrgRepo orgRepo,
            BookRequestRepository bookRequestRepository,
            DonationRepository donationRepository,
            CompetitionRepo competitionRepo) {
        this.orgRepo = orgRepo;
        this.bookRequestRepository = bookRequestRepository;
        this.donationRepository = donationRepository;
        this.competitionRepo = competitionRepo;
    }

    @Override
    public DashboardStatsDTO getDashboardStats(Long orgId) {
        try {
            // Verify organization exists
            if (!orgRepo.existsByOrgId(orgId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found with orgId: " + orgId);
            }

            // Previous month stats for comparison
            YearMonth previousMonth = YearMonth.now().minusMonths(1);
            LocalDateTime startOfPrevMonth = previousMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfPrevMonth = previousMonth.atEndOfMonth().atTime(23, 59, 59);

            // Get current counts with error handling
            Long pendingRequestsCount = bookRequestRepository.countByOrganizationOrgIdAndStatus(orgId, "PENDING");
            long pendingRequests = pendingRequestsCount != null ? pendingRequestsCount : 0L;

            long booksReceived = donationRepository.countBooksByOrganizationIdAndStatus(orgId, "RECEIVED");

            long upcomingEvents = 5; // Mock data - would come from an events repository

            Long totalDonationsCount = donationRepository.countByOrganizationId(orgId);
            long totalDonations = totalDonationsCount != null ? totalDonationsCount : 0L;

            // Get previous month counts for calculating change percentage
            long prevMonthPendingRequests = 0L;
            long prevMonthBooksReceived = 0L;
            long prevMonthTotalDonations = 0L;

            try {
                Long prevMonthPendingRequestsCount = bookRequestRepository.countByOrganizationOrgIdAndStatusAndDateRequestedBetween(
                        orgId, "PENDING", startOfPrevMonth, endOfPrevMonth);
                prevMonthPendingRequests = prevMonthPendingRequestsCount != null ? prevMonthPendingRequestsCount : 0L;
            } catch (Exception e) {
                System.err.println("Error fetching previous month pending requests: " + e.getMessage());
            }

            try {
                prevMonthBooksReceived = donationRepository.countBooksByOrganizationIdAndStatusAndDateReceivedBetween(
                        orgId, "RECEIVED", startOfPrevMonth, endOfPrevMonth);
            } catch (Exception e) {
                System.err.println("Error fetching previous month books received: " + e.getMessage());
            }

            try {
                Long prevMonthTotalDonationsCount = donationRepository.countByOrganizationIdAndDateDonatedBetween(
                        orgId, startOfPrevMonth, endOfPrevMonth);
                prevMonthTotalDonations = prevMonthTotalDonationsCount != null ? prevMonthTotalDonationsCount : 0L;
            } catch (Exception e) {
                System.err.println("Error fetching previous month total donations: " + e.getMessage());
            }

            // Calculate percentage changes
            int pendingRequestsChange = calculatePercentageChange(prevMonthPendingRequests, pendingRequests);
            int booksReceivedChange = calculatePercentageChange(prevMonthBooksReceived, booksReceived);
            int totalDonationsChange = calculatePercentageChange(prevMonthTotalDonations, totalDonations);

            // Build the stats DTO
            DashboardStatsDTO stats = new DashboardStatsDTO();
            stats.setPendingRequests(pendingRequests);
            stats.setBooksReceived(booksReceived);
            stats.setUpcomingEvents(upcomingEvents);
            stats.setTotalDonations(totalDonations);

            stats.setPendingRequestsChange(pendingRequestsChange);
            stats.setBooksReceivedChange(booksReceivedChange);
            stats.setTotalDonationsChange(totalDonationsChange);
            stats.setUpcomingEventsChange(0); // No change data for events in this example

            return stats;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error in getDashboardStats for orgId " + orgId + ": " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch dashboard stats: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RecentRequestsDTO> getRecentRequests(Long orgId) {
        if (!orgRepo.existsByOrgId(orgId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found with orgId: " + orgId);
        }

        List<BookRequest> recentRequests = bookRequestRepository.findRecentByOrganizationId(orgId, 5);
        return recentRequests.stream()
                .map(this::mapToRecentRequestDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UpcomingEventsDTO> getUpcomingEvents(Long orgId) {
        if (!orgRepo.existsByOrgId(orgId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found with orgId: " + orgId);
        }

        // Mock data - in a real application, this would come from an events repository
        LocalDateTime today = LocalDateTime.now();

        UpcomingEventsDTO event1 = new UpcomingEventsDTO();
        event1.setId(1L);
        event1.setTitle("Book Donation Drive");
        event1.setDate(today.plusDays(7).toLocalDate().toString());
        event1.setTime("14:00:00");
        event1.setLocation("Main Campus");

        UpcomingEventsDTO event2 = new UpcomingEventsDTO();
        event2.setId(2L);
        event2.setTitle("Reading Workshop");
        event2.setDate(today.plusDays(14).toLocalDate().toString());
        event2.setTime("10:00:00");
        event2.setLocation("Library Hall");

        UpcomingEventsDTO event3 = new UpcomingEventsDTO();
        event3.setId(3L);
        event3.setTitle("Author Meet & Greet");
        event3.setDate(today.plusDays(21).toLocalDate().toString());
        event3.setTime("16:00:00");
        event3.setLocation("Conference Room");

        return List.of(event1, event2, event3);
    }

    @Override
    public List<TopDonorDTO> getTopDonors(Long orgId) {
        try {
            // Verify organization exists
            if (!orgRepo.existsByOrgId(orgId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found with orgId: " + orgId);
            }

            // Get top donors from repository
            List<TopDonorDTO> topDonors = donationRepository.findTopDonorsByOrganization(orgId);
            
            System.out.println("Found " + topDonors.size() + " top donors for orgId: " + orgId);
            
            return topDonors;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error in getTopDonors for orgId " + orgId + ": " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch top donors: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ActiveCompetitionDTO> getActiveCompetitions() {
        try {
            // Get active competitions from repository
            List<Competitions> activeCompetitions = competitionRepo.findActiveCompetitions();
            
            System.out.println("Found " + activeCompetitions.size() + " active competitions");
            
            // Map to DTOs
            return activeCompetitions.stream()
                    .map(this::mapToActiveCompetitionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getActiveCompetitions: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch active competitions: " + e.getMessage(), e);
        }
    }

    private int calculatePercentageChange(long previous, long current) {
        if (previous == 0) {
            return current > 0 ? 100 : 0;
        }
        return (int) (((current - previous) / (double) previous) * 100);
    }

    private RecentRequestsDTO mapToRecentRequestDTO(BookRequest request) {
        RecentRequestsDTO dto = new RecentRequestsDTO();
        dto.setId(request.getId());
        dto.setTitle(request.getTitle());
        dto.setSubject(request.getSubject());
        dto.setQuantity(request.getQuantity());
        dto.setStatus(request.getStatus());
        dto.setDateRequested(request.getDateRequested().toString());
        return dto;
    }

    private ActiveCompetitionDTO mapToActiveCompetitionDTO(Competitions competition) {
        ActiveCompetitionDTO dto = new ActiveCompetitionDTO();
        dto.setCompetitionId(competition.getCompetitionId());
        dto.setActiveStatus(competition.isActiveStatus());
        dto.setPauseStatus(competition.getPauseStatus());
        dto.setTitle(competition.getTitle());
        dto.setTheme(competition.getTheme());
        dto.setDescription(competition.getDescription());
        dto.setPrizeTrustScore(competition.getPrizeTrustScore());
        dto.setEntryTrustScore(competition.getEntryTrustScore());
        dto.setStartDateTime(competition.getStartDateTime());
        dto.setEndDateTime(competition.getEndDateTime());
        dto.setVotingStartDateTime(competition.getVotingStartDateTime());
        dto.setVotingEndDateTime(competition.getVotingEndDateTime());
        dto.setMaxParticipants(competition.getMaxParticipants());
        dto.setCurrentParticipants(competition.getCurrentParticipants());
        dto.setBannerImage(competition.getBannerImage());
        dto.setCreatedAt(competition.getCreatedAt());
        dto.setActivatedAt(competition.getActivatedAt());
        return dto;
    }
}