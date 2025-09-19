// DonationServiceImpl.java
package service.organization.impl;

import model.dto.Organization.DonationDto.*;
import model.entity.Donation;
import model.entity.Organization;
import model.repo.organization.DonationRepository;
import model.repo.organization.OrganizationRepository;
import model.repo.organization.NotificationRepository;
import service.organization.DonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final OrganizationRepository organizationRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public List<DonationResponseDto> getDonationsByOrganization(Long orgId) {
        log.info("Fetching donations for organization ID: {}", orgId);
        
        return donationRepository.findByOrganizationIdOrderByDonationDateDesc(orgId)
                .stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    @Override
    public DonationResponseDto markDonationAsReceived(Long donationId, DonationConfirmationDto confirmationData) {
        log.info("Marking donation with ID: {} as received", donationId);
        
        // Validate organization exists
        Organization organization = organizationRepository.findById(confirmationData.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        
        // Validate the donation belongs to the organization
        if (!donation.getOrganizationId().equals(confirmationData.getOrganizationId())) {
            throw new RuntimeException("Donation does not belong to this organization");
        }
        
        // Update donation status
        donation.setStatus("RECEIVED");
        donation.setReceivedDate(LocalDateTime.now());
        donation.setCondition(confirmationData.getIsConditionSatisfactory() ? "GOOD" : "DAMAGED");
        if (confirmationData.getNotes() != null) {
            donation.setNotes(confirmationData.getNotes());
        }
        
        Donation updatedDonation = donationRepository.save(donation);
        log.info("Marked donation with ID: {} as received", donationId);
        
        // Create notification for donor
        createNotification(
            null, // Donor notification - organization ID not needed
            "Donation received",
            "Your donation to " + organization.getName() + " has been received",
            "DONATION_RECEIVED",
            donationId
        );
        
        return convertToResponseDto(updatedDonation);
    }

    @Override
    public List<PendingDonationDto> getPendingDonations(Long orgId) {
        log.info("Fetching pending donations for organization ID: {}", orgId);
        
        return donationRepository.findByOrganizationIdAndStatusOrderByDonationDateDesc(orgId, "IN_TRANSIT")
                .stream()
                .map(this::convertToPendingDonationDto)
                .toList();
    }
    
    // Helper methods
    private DonationResponseDto convertToResponseDto(Donation donation) {
        DonationResponseDto dto = new DonationResponseDto();
        dto.setId(donation.getId());
        dto.setOrganizationId(donation.getOrganizationId());
        dto.setDonorName(donation.getDonorName());
        dto.setDonationType(donation.getDonationType());
        dto.setQuantity(donation.getQuantity());
        dto.setValue(donation.getValue());
        dto.setStatus(donation.getStatus());
        dto.setDonationDate(donation.getDonationDate());
        dto.setReceivedDate(donation.getReceivedDate());
        dto.setCondition(donation.getCondition());
        dto.setNotes(donation.getNotes());
        dto.setBookTitle(donation.getBookTitle());
        dto.setBookAuthor(donation.getBookAuthor());
        dto.setTrackingNumber(donation.getTrackingNumber());
        
        // Get organization name
        organizationRepository.findById(donation.getOrganizationId())
                .ifPresent(org -> dto.setOrganizationName(org.getName()));
        
        return dto;
    }
    
    private PendingDonationDto convertToPendingDonationDto(Donation donation) {
        PendingDonationDto dto = new PendingDonationDto();
        dto.setId(donation.getId());
        dto.setDonorName(donation.getDonorName());
        dto.setDonationType(donation.getDonationType());
        dto.setQuantity(donation.getQuantity());
        dto.setDonationDate(donation.getDonationDate());
        dto.setBookTitle(donation.getBookTitle());
        dto.setStatus(donation.getStatus());
        dto.setValue(donation.getValue());
        return dto;
    }
    
    private void createNotification(Long userId, String title, String message, String type, Long referenceId) {
        try {
            // This is a placeholder implementation - replace with your actual notification logic
            if (notificationRepository != null) {
                notificationRepository.createNotification(userId, title, message, type, referenceId);
            }
        } catch (Exception e) {
            log.error("Failed to create notification", e);
        }
    }
}