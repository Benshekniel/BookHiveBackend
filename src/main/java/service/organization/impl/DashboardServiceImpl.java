// DashboardServiceImpl.java
package service.organization.impl;

import model.dto.Organization.DashboardDto.*;
import model.repo.organization.BookRequestRepository;
import model.repo.organization.DonationRepository;
import model.repo.organization.EventRepository;
import model.repo.organization.NotificationRepository;
import model.repo.organization.OrganizationRepository;
import service.organization.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private final OrganizationRepository organizationRepository;
    private final BookRequestRepository bookRequestRepository;
    private final DonationRepository donationRepository;
    private final NotificationRepository notificationRepository;
    private final EventRepository eventRepository;

    @Override
    public Optional<DashboardStatsDto> getStats(Long orgId) {
        log.info("Fetching dashboard stats for organization ID: {}", orgId);
        
        if (!organizationRepository.existsById(orgId)) {
            log.warn("Organization not found with ID: {}", orgId);
            return Optional.empty();
        }
        
        // Gather dashboard statistics
        int totalRequests = bookRequestRepository.countByOrganizationId(orgId);
        int pendingRequests = bookRequestRepository.countByOrganizationIdAndStatus(orgId, "PENDING");
        int completedRequests = bookRequestRepository.countByOrganizationIdAndStatus(orgId, "COMPLETED");
        int activeRequests = bookRequestRepository.countByOrganizationIdAndStatus(orgId, "ACTIVE");
        int rejectedRequests = bookRequestRepository.countByOrganizationIdAndStatus(orgId, "REJECTED");
        int totalDonations = donationRepository.countByOrganizationId(orgId);
        Double donationValue = donationRepository.sumValueByOrganizationId(orgId);
        int unreadNotifications = notificationRepository.countByOrganizationIdAndReadFalse(orgId);
        Double averageRating = donationRepository.getAverageFeedbackRating(orgId);
        
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalRequests(totalRequests);
        stats.setPendingRequests(pendingRequests);
        stats.setCompletedRequests(completedRequests);
        stats.setActiveRequests(activeRequests);
        stats.setRejectedRequests(rejectedRequests);
        stats.setTotalDonations(totalDonations);
        stats.setDonationValue(donationValue != null ? donationValue : 0.0);
        stats.setUnreadNotifications(unreadNotifications);
        stats.setAverageRating(averageRating != null ? averageRating : 0.0);
        
        return Optional.of(stats);
    }

    @Override
    public Optional<RecentRequestsResponseDto> getRecentRequests(Long orgId) {
        log.info("Fetching recent requests for organization ID: {}", orgId);
        
        if (!organizationRepository.existsById(orgId)) {
            log.warn("Organization not found with ID: {}", orgId);
            return Optional.empty();
        }
        
        List<RecentRequestItem> recentRequests = bookRequestRepository.findTop5ByOrganizationIdOrderByCreatedAtDesc(orgId)
                .stream()
                .map(request -> {
                    RecentRequestItem item = new RecentRequestItem();
                    item.setId(request.getId());
                    item.setTitle(request.getTitle());
                    item.setStatus(request.getStatus());
                    item.setCreatedAt(request.getCreatedAt());
                    item.setQuantity(request.getQuantity());
                    item.setCategory(request.getCategories() != null && !request.getCategories().isEmpty() ? 
                                     request.getCategories().get(0) : "General");
                    return item;
                })
                .toList();
        
        RecentRequestsResponseDto responseDto = new RecentRequestsResponseDto();
        responseDto.setRequests(recentRequests);
        responseDto.setTotalCount(bookRequestRepository.countByOrganizationId(orgId));
        
        return Optional.of(responseDto);
    }

    @Override
    public Optional<UpcomingEventsResponseDto> getUpcomingEvents(Long orgId) {
        log.info("Fetching upcoming events for organization ID: {}", orgId);
        
        if (!organizationRepository.existsById(orgId)) {
            log.warn("Organization not found with ID: {}", orgId);
            return Optional.empty();
        }
        
        List<EventItem> upcomingEvents = eventRepository.findUpcomingEventsByOrganizationId(orgId, LocalDateTime.now())
                .stream()
                .map(event -> {
                    EventItem item = new EventItem();
                    item.setId(event.getId());
                    item.setTitle(event.getTitle());
                    item.setDescription(event.getDescription());
                    item.setEventDate(event.getEventDate());
                    item.setLocation(event.getLocation());
                    item.setEventType(event.getEventType());
                    return item;
                })
                .toList();
        
        UpcomingEventsResponseDto responseDto = new UpcomingEventsResponseDto();
        responseDto.setEvents(upcomingEvents);
        responseDto.setTotalCount(eventRepository.countByOrganizationId(orgId));
        
        return Optional.of(responseDto);
    }
}