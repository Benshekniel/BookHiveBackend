// OrganizationServiceImpl.java
package service.organization.impl;

import model.dto.Organization.OrganizationDto.*;
import model.dto.Organization.DashboardDto.*;
import model.entity.Organization;
import model.entity.AllUsers;
import model.repo.organization.OrganizationRepository;
import model.repo.organization.BookRequestRepository;
import model.repo.organization.DonationRepository;
import model.repo.organization.EventRepository;
import model.repo.organization.NotificationRepository;
import model.repo.AllUsersRepo;
import service.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
// Replace the Lombok Slf4j annotation with explicit logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    // Explicitly define the logger
    private static final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);
    
    private final OrganizationRepository organizationRepository;
    private final BookRequestRepository bookRequestRepository;
    private final DonationRepository donationRepository;
    private final NotificationRepository notificationRepository;
    private final EventRepository eventRepository;
    private final AllUsersRepo allUsersRepo;
    private final PasswordEncoder passwordEncoder;
    
    private static final String UPLOAD_DIR = "uploads/organizations/";

   


    @Override
    public Optional<OrganizationResponseDto> getOrganizationProfile(Long orgId) {
        log.info("Fetching organization profile for ID: {}", orgId);
        return organizationRepository.findById(orgId)
                .map(this::convertToResponseDto);
    }

    @Override
    public OrganizationResponseDto updateOrganizationProfile(Long orgId, OrganizationUpdateDto updateDto) {
        log.info("Updating organization profile for ID: {}", orgId);
        
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        // Update fields
        if (updateDto.getName() != null) {
            organization.setName(updateDto.getName());
        }
        if (updateDto.getEmail() != null) {
            organization.setEmail(updateDto.getEmail());
        }
        if (updateDto.getPhone() != null) {
            organization.setPhone(updateDto.getPhone());
        }
        if (updateDto.getAddress() != null) {
            organization.setAddress(updateDto.getAddress());
        }
        if (updateDto.getDescription() != null) {
            organization.setDescription(updateDto.getDescription());
        }
        if (updateDto.getWebsiteUrl() != null) {
            organization.setWebsiteUrl(updateDto.getWebsiteUrl());
        }
        if (updateDto.getContactPerson() != null) {
            organization.setContactPerson(updateDto.getContactPerson());
        }
        
        organization.setUpdatedAt(LocalDateTime.now());
        Organization updatedOrg = organizationRepository.save(organization);
        
        // Update user details if necessary
        if (organization.getUserId() != null && updateDto.getName() != null) {
            allUsersRepo.findById(organization.getUserId().intValue())
                    .ifPresent(user -> {
                        user.setName(updateDto.getName());
                        allUsersRepo.save(user);
                    });
        }
        
        log.info("Organization profile updated for ID: {}", orgId);
        return convertToResponseDto(updatedOrg);
    }

    @Override
    public Optional<OrganizationStatisticsDto> getOrganizationStatistics(Long orgId) {
        log.info("Fetching statistics for organization ID: {}", orgId);
        
        if (!organizationRepository.existsById(orgId)) {
            log.warn("Organization not found with ID: {}", orgId);
            return Optional.empty();
        }
        
        // Gather statistics
        int totalBookRequests = bookRequestRepository.countByOrganizationId(orgId);
        int pendingRequests = bookRequestRepository.countByOrganizationIdAndStatus(orgId, "PENDING");
        int completedRequests = bookRequestRepository.countByOrganizationIdAndStatus(orgId, "COMPLETED");
        int totalDonationsReceived = donationRepository.countByOrganizationId(orgId);
        Double donationValue = donationRepository.sumValueByOrganizationId(orgId);
        Double averageFeedbackRating = donationRepository.getAverageFeedbackRating(orgId);
        int totalEvents = eventRepository.countByOrganizationId(orgId);
        int unreadNotifications = notificationRepository.countByOrganizationIdAndReadFalse(orgId);
        
        OrganizationStatisticsDto stats = new OrganizationStatisticsDto();
        stats.setTotalBookRequests(totalBookRequests);
        stats.setPendingRequests(pendingRequests);
        stats.setCompletedRequests(completedRequests);
        stats.setTotalDonationsReceived(totalDonationsReceived);
        stats.setDonationValue(donationValue != null ? donationValue : 0.0);
        stats.setAverageFeedbackRating(averageFeedbackRating != null ? averageFeedbackRating : 0.0);
        stats.setTotalEvents(totalEvents);
        stats.setUnreadNotifications(unreadNotifications);
        
        return Optional.of(stats);
    }

    @Override
    public ImageUploadResponseDto uploadOrganizationImage(Long orgId, MultipartFile imageFile) {
        log.info("Uploading image for organization ID: {}", orgId);
        
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        try {
            // Create upload directory if it doesn't exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Generate unique filename
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".") 
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                    : ".jpg";
            String newFilename = "org_" + orgId + "_" + UUID.randomUUID() + fileExtension;
            
            // Save file
            Path targetLocation = Paths.get(UPLOAD_DIR + newFilename);
            Files.copy(imageFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // Update organization's image URL
            String imageUrl = "/api/uploads/organizations/" + newFilename;
            organization.setImageUrl(imageUrl);
            organization.setUpdatedAt(LocalDateTime.now());
            organizationRepository.save(organization);
            
            log.info("Image uploaded for organization ID: {}", orgId);
            
            ImageUploadResponseDto response = new ImageUploadResponseDto();
            response.setImageUrl(imageUrl);
            response.setMessage("Image uploaded successfully");
            response.setSuccess(true);
            return response;
            
        } catch (IOException e) {
            log.error("Failed to upload image for organization ID: {}", orgId, e);
            ImageUploadResponseDto response = new ImageUploadResponseDto();
            response.setImageUrl(null);
            response.setMessage("Failed to upload image: " + e.getMessage());
            response.setSuccess(false);
            return response;
        }
    }

    @Override
    public PasswordChangeResponseDto changePassword(Long orgId, PasswordChangeRequestDto passwordData) {
        log.info("Changing password for organization ID: {}", orgId);
        
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        // Ensure organization has a userId
        if (organization.getUserId() == null) {
            log.error("Organization has no associated user ID");
            return new PasswordChangeResponseDto(false, "Organization has no associated user account");
        }
        
        // Find user
        AllUsers user = allUsersRepo.findById(organization.getUserId().intValue())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if current password matches
        if (!passwordEncoder.matches(passwordData.getCurrentPassword(), user.getPassword())) {
            log.warn("Current password doesn't match for organization ID: {}", orgId);
            return new PasswordChangeResponseDto(false, "Current password is incorrect");
        }
        
        // Check if new password and confirm password match
        if (!passwordData.getNewPassword().equals(passwordData.getConfirmPassword())) {
            log.warn("New password and confirm password don't match for organization ID: {}", orgId);
            return new PasswordChangeResponseDto(false, "New password and confirm password don't match");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(passwordData.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        allUsersRepo.save(user);
        
        log.info("Password changed successfully for organization ID: {}", orgId);
        return new PasswordChangeResponseDto(true, "Password changed successfully");
    }

    @Override
    public TwoFactorAuthResponseDto toggleTwoFactorAuth(Long orgId, boolean enable) {
        log.info("Toggling two-factor authentication for organization ID: {}: {}", orgId, enable ? "enable" : "disable");
        
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        organization.setTwoFactorEnabled(enable);
        organization.setUpdatedAt(LocalDateTime.now());
        organizationRepository.save(organization);
        
        TwoFactorAuthResponseDto response = new TwoFactorAuthResponseDto();
        response.setEnabled(enable);
        response.setMessage(enable ? "Two-factor authentication enabled" : "Two-factor authentication disabled");
        
        // If enabling, generate a setup key (this is just a placeholder - implement actual 2FA setup logic)
        if (enable) {
            response.setSetupKey("ABCDEF123456");
        }
        
        log.info("Two-factor authentication toggled for organization ID: {}", orgId);
        return response;
    }

    @Override
    public Optional getDashboardStats(Long orgId) {
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
    public Optional getRecentRequests(Long orgId) {
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
    public Optional getUpcomingEvents(Long orgId) {
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
    
    // Helper methods
    private OrganizationResponseDto convertToResponseDto(Organization organization) {
        OrganizationResponseDto dto = new OrganizationResponseDto();
        dto.setId(organization.getId());
        dto.setName(organization.getName());
        dto.setEmail(organization.getEmail());
        dto.setPhone(organization.getPhone());
        dto.setAddress(organization.getAddress());
        dto.setDescription(organization.getDescription());
        dto.setImageUrl(organization.getImageUrl());
        dto.setTwoFactorEnabled(organization.isTwoFactorEnabled());
        dto.setWebsiteUrl(organization.getWebsiteUrl());
        dto.setContactPerson(organization.getContactPerson());
        dto.setRegistrationNumber(organization.getRegistrationNumber());
        dto.setCreatedAt(organization.getCreatedAt());
        dto.setUpdatedAt(organization.getUpdatedAt());
        
        // Add additional stats
        dto.setTotalBookRequests(bookRequestRepository.countByOrganizationId(organization.getId()));
        dto.setTotalDonations(donationRepository.countByOrganizationId(organization.getId()));
        dto.setAverageRating(donationRepository.getAverageFeedbackRating(organization.getId()));
        
        return dto;
    }
}