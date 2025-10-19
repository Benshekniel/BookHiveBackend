

        package service.organization.impl;

import model.dto.organization.DashboardStatsDTO;
import model.dto.organization.RecentRequestsDTO;
import model.dto.organization.UpcomingEventsDTO;
import model.entity.BookRequest;
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

    @Autowired
    public OrganizationDashboardServiceImpl(
            OrgRepo orgRepo,
            BookRequestRepository bookRequestRepository,
            DonationRepository donationRepository) {
        this.orgRepo = orgRepo;
        this.bookRequestRepository = bookRequestRepository;
        this.donationRepository = donationRepository;
    }

    @Override
    public DashboardStatsDTO getDashboardStats(Long orgId) {
        // Verify organization exists
        if (!orgRepo.existsByOrgId(orgId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found with orgId: " + orgId);
        }

        // Current month stats
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);

        // Previous month stats for comparison
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        LocalDateTime startOfPrevMonth = previousMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfPrevMonth = previousMonth.atEndOfMonth().atTime(23, 59, 59);

        // Get current counts
        Long pendingRequestsCount = bookRequestRepository.countByOrganizationOrgIdAndStatus(orgId, "PENDING");
        long pendingRequests = pendingRequestsCount != null ? pendingRequestsCount : 0L;

        long booksReceived = donationRepository.countBooksByOrganizationIdAndStatus(orgId, "RECEIVED");

        long upcomingEvents = 5; // Mock data - would come from an events repository

        Long totalDonationsCount = donationRepository.countByOrganizationId(orgId);
        long totalDonations = totalDonationsCount != null ? totalDonationsCount : 0L;

        // Get previous month counts for calculating change percentage
        Long prevMonthPendingRequestsCount = bookRequestRepository.countByOrganizationOrgIdAndStatusAndDateRequestedBetween(
                orgId, "PENDING", startOfPrevMonth, endOfPrevMonth);
        long prevMonthPendingRequests = prevMonthPendingRequestsCount != null ? prevMonthPendingRequestsCount : 0L;

        long prevMonthBooksReceived = donationRepository.countBooksByOrganizationIdAndStatusAndDateReceivedBetween(
                orgId, "RECEIVED", startOfPrevMonth, endOfPrevMonth);

        Long prevMonthTotalDonationsCount = donationRepository.countByOrganizationIdAndDateDonatedBetween(
                orgId, startOfPrevMonth, endOfPrevMonth);
        long prevMonthTotalDonations = prevMonthTotalDonationsCount != null ? prevMonthTotalDonationsCount : 0L;

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
}