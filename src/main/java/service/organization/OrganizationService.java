package service.organization;

import model.dto.Organization.DashboardDto;
import model.dto.Organization.OrganizationDto.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

public interface OrganizationService {
    
    // Get organization profile by ID
    Optional<OrganizationResponseDto> getOrganizationProfile(Long orgId);
    
    // Update organization profile
    OrganizationResponseDto updateOrganizationProfile(Long orgId, OrganizationUpdateDto updateDto);
    
    // Get organization statistics
    Optional<OrganizationStatisticsDto> getOrganizationStatistics(Long orgId);
    
    // Upload organization image
    ImageUploadResponseDto uploadOrganizationImage(Long orgId, MultipartFile imageFile);
    
    // Change organization password
    PasswordChangeResponseDto changePassword(Long orgId, PasswordChangeRequestDto passwordData);
    
    // Toggle two-factor authentication
    TwoFactorAuthResponseDto toggleTwoFactorAuth(Long orgId, boolean enable);
    
    // Get dashboard stats
    Optional<DashboardDto.DashboardStatsDto> getDashboardStats(Long orgId);
    
    // Get recent requests
    Optional<DashboardDto.RecentRequestsResponseDto> getRecentRequests(Long orgId);
    
    // Get upcoming events
    Optional<DashboardDto.UpcomingEventsResponseDto> getUpcomingEvents(Long orgId);
}