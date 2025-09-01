package service.organization.impl;

import model.dto.organization.DonationReceivedDTO;
import model.dto.organization.DonationConfirmationDTO;
import org.springframework.stereotype.Service;
import service.organization.DonationService;
import java.util.List;

@Service
public class DonationServiceImpl implements DonationService {

    @Override
    public List<DonationReceivedDTO> getDonationsByOrganization(Long orgId) {
    // TODO: Map entities to DTOs
    return java.util.Collections.emptyList();
    }

    @Override
    public DonationReceivedDTO markAsReceived(Long donationId, DonationConfirmationDTO dto) {
        // TODO: Implement logic to mark donation as received
        return null;
    }
}
