package service.organization;

import model.dto.organization.DonationReceivedDTO;
import model.dto.organization.DonationConfirmationDTO;
import java.util.List;

public interface DonationService {
    List<DonationReceivedDTO> getDonationsByOrganization(Long orgId);
    DonationReceivedDTO markAsReceived(Long donationId, DonationConfirmationDTO dto);
}
