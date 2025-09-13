package service.organization;

import model.dto.Organization.DonationDto.*;
import java.util.List;

public interface DonationService {
    
    // Get all donations for an organization
    List<DonationResponseDto> getDonationsByOrganization(Long orgId);
    
    // Mark donation as received
    DonationResponseDto markDonationAsReceived(Long donationId, DonationConfirmationDto confirmationData);
    
    // Get pending donations for feedback
    List<PendingDonationDto> getPendingDonations(Long orgId);
}