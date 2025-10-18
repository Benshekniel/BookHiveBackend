package service.organization.impl;

import model.dto.organization.DashboardStatsDTO;
import model.dto.organization.RecentRequestsDTO;
import model.dto.organization.UpcomingEventsDTO;
import model.entity.BookRequest;
import model.repo.organization.BookRequestRepository;
import model.repo.organization.DonationRepository;
import model.repo.organization.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.organization.OrganizationDashboardService;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationDashboardServiceImpl implements OrganizationDashboardService {

    private final OrganizationRepository organizationRepository;
    private final BookRequestRepository bookRequestRepository;
    private final DonationRepository donationRepository;

    @Autowired
    public OrganizationDashboardServiceImpl(
            OrganizationRepository organizationRepository,
            BookRequestRepository bookRequestRepository,
            DonationRepository donationRepository) {
        this.organizationRepository = organizationRepository;
        this.bookRequestRepository = bookRequestRepository;
        this.donationRepository = donationRepository;
    }

    @Override
    public DashboardStatsDTO getDashboardStats(Long organizationId) {
        // Verify organization exists
        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException("Organization not found with id: " + organizationId);
        }

        // Current month stats
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);

        // Previous month stats for comparison
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        LocalDateTime startOfPrevMonth = previousMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfPrevMonth = previousMonth.atEndOfMonth().atTime(23, 59, 59);

        // Get current counts
        Long pendingRequestsCount = bookRequestRepository.countByOrganizationIdAndStatus(organizationId, "PENDING");
        long pendingRequests = pendingRequestsCount != null ? pendingRequestsCount : 0L;

        long booksReceived = donationRepository.countBooksByOrganizationIdAndStatus(organizationId, "RECEIVED");

        long upcomingEvents = 5; // Mock data - would come from an events repository

        Long totalDonationsCount = donationRepository.countByOrganizationId(organizationId);
        long totalDonations = totalDonationsCount != null ? totalDonationsCount : 0L;

        // Get previous month counts for calculating change percentage
        Long prevMonthPendingRequestsCount = bookRequestRepository.countByOrganizationIdAndStatusAndDateRequestedBetween(
                organizationId, "PENDING", startOfPrevMonth, endOfPrevMonth);
        long prevMonthPendingRequests = prevMonthPendingRequestsCount != null ? prevMonthPendingRequestsCount : 0L;

        long prevMonthBooksReceived = donationRepository.countBooksByOrganizationIdAndStatusAndDateReceivedBetween(
                organizationId, "RECEIVED", startOfPrevMonth, endOfPrevMonth);

        Long prevMonthTotalDonationsCount = donationRepository.countByOrganizationIdAndDateDonatedBetween(
                organizationId, startOfPrevMonth, endOfPrevMonth);
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
    public List<RecentRequestsDTO> getRecentRequests(Long organizationId) {
        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException("Organization not found with id: " + organizationId);
        }

        List<BookRequest> recentRequests = bookRequestRepository.findRecentByOrganizationId(organizationId, 5);
        return recentRequests.stream()
                .map(this::mapToRecentRequestDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UpcomingEventsDTO> getUpcomingEvents(Long organizationId) {
        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException("Organization not found with id: " + organizationId);
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
}//package service.organization.impl;
//
//
//import model.dto.organization.DashboardStatsDTO;
//import model.dto.organization.RecentRequestsDTO;
//import model.dto.organization.UpcomingEventsDTO;
//import model.entity.BookRequest;
//import model.entity.Organization;
//import model.repo.organization.BookRequestRepository;
//import model.repo.organization.DonationRepository;
//import model.repo.organization.OrganizationRepository;
//import service.organization.OrganizationDashboardService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.YearMonth;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class OrganizationDashboardServiceImpl implements OrganizationDashboardService {
//
//    private final OrganizationRepository organizationRepository;
//    private final BookRequestRepository bookRequestRepository;
//    private final DonationRepository donationRepository;
//
//    @Autowired
//    public OrganizationDashboardServiceImpl(
//            OrganizationRepository organizationRepository,
//            BookRequestRepository bookRequestRepository,
//            DonationRepository donationRepository) {
//        this.organizationRepository = organizationRepository;
//        this.bookRequestRepository = bookRequestRepository;
//        this.donationRepository = donationRepository;
//    }
//
//    @Override
//    public DashboardStatsDTO getDashboardStats(Long organizationId) {
//        // Verify organization exists
//        Organization organization = organizationRepository.findById(organizationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + organizationId));
//
//        // Current month stats
//        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
//        LocalDateTime endOfMonth = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);
//
//        // Previous month stats for comparison
//        YearMonth previousMonth = YearMonth.now().minusMonths(1);
//        LocalDateTime startOfPrevMonth = previousMonth.atDay(1).atStartOfDay();
//        LocalDateTime endOfPrevMonth = previousMonth.atEndOfMonth().atTime(23, 59, 59);
//
//        // Get current counts
//        long pendingRequests = bookRequestRepository.countByOrganizationIdAndStatus(organizationId, "PENDING");
//        long booksReceived = donationRepository.countBooksByOrganizationIdAndStatus(organizationId, "RECEIVED");
//        long upcomingEvents = 5; // Mock data - would come from an events repository
//        long totalDonations = donationRepository.countByOrganizationId(organizationId);
//
//        // Get previous month counts for calculating change percentage
//        long prevMonthPendingRequests = bookRequestRepository.countByOrganizationIdAndStatusAndDateRequestedBetween(
//                organizationId, "PENDING", startOfPrevMonth, endOfPrevMonth);
//        long prevMonthBooksReceived = donationRepository.countBooksByOrganizationIdAndStatusAndDateReceivedBetween(
//                organizationId, "RECEIVED", startOfPrevMonth, endOfPrevMonth);
//        long prevMonthTotalDonations = donationRepository.countByOrganizationIdAndDateDonatedBetween(
//                organizationId, startOfPrevMonth, endOfPrevMonth);
//
//        // Calculate percentage changes
//        int pendingRequestsChange = calculatePercentageChange(prevMonthPendingRequests, pendingRequests);
//        int booksReceivedChange = calculatePercentageChange(prevMonthBooksReceived, booksReceived);
//        int totalDonationsChange = calculatePercentageChange(prevMonthTotalDonations, totalDonations);
//
//        // Build the stats DTO
//        DashboardStatsDTO stats = new DashboardStatsDTO();
//        stats.setPendingRequests(pendingRequests);
//        stats.setBooksReceived(booksReceived);
//        stats.setUpcomingEvents(upcomingEvents);
//        stats.setTotalDonations(totalDonations);
//
//        stats.setPendingRequestsChange(pendingRequestsChange);
//        stats.setBooksReceivedChange(booksReceivedChange);
//        stats.setTotalDonationsChange(totalDonationsChange);
//        stats.setUpcomingEventsChange(0); // No change data for events in this example
//
//        return stats;
//    }
//
//    @Override
//    public List<RecentRequestsDTO> getRecentRequests(Long organizationId) {
//        // Get recent book requests
//        List<BookRequest> recentRequests = bookRequestRepository.findRecentByOrganizationId(
//                organizationId, 5); // Get top 5 recent requests
//
//        // Map to DTOs
//        return recentRequests.stream()
//                .map(this::mapToRecentRequestDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<UpcomingEventsDTO> getUpcomingEvents(Long organizationId) {
//        // Mock data - in a real application, this would come from an events repository
//        LocalDate today = LocalDate.now();
//
//        // Create some example upcoming events
//        UpcomingEventsDTO event1 = new UpcomingEventsDTO();
//        event1.setId(1L);
//        event1.setTitle("Book Donation Drive");
//        event1.setDate(today.plusDays(7).toString());
//        event1.setTime("14:00:00");
//        event1.setLocation("Main Campus");
//
//        UpcomingEventsDTO event2 = new UpcomingEventsDTO();
//        event2.setId(2L);
//        event2.setTitle("Reading Workshop");
//        event2.setDate(today.plusDays(14).toString());
//        event2.setTime("10:00:00");
//        event2.setLocation("Library Hall");
//
//        UpcomingEventsDTO event3 = new UpcomingEventsDTO();
//        event3.setId(3L);
//        event3.setTitle("Author Meet & Greet");
//        event3.setDate(today.plusDays(21).toString());
//        event3.setTime("16:00:00");
//        event3.setLocation("Conference Room");
//
//        return List.of(event1, event2, event3);
//    }
//
//    private int calculatePercentageChange(long previous, long current) {
//        if (previous == 0) {
//            return current > 0 ? 100 : 0;
//        }
//        return (int) (((current - previous) / (double) previous) * 100);
//    }
//
//    private RecentRequestsDTO mapToRecentRequestDTO(BookRequest request) {
//        RecentRequestsDTO dto = new RecentRequestsDTO();
//        dto.setId(request.getId());
//        dto.setTitle(request.getTitle());
//        dto.setSubject(request.getSubject());
//        dto.setQuantity(request.getQuantity());
//        dto.setStatus(request.getStatus());
//        dto.setDateRequested(request.getDateRequested().toString());
//        return dto;
//    }
//}