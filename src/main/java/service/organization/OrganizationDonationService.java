package service.organization;

import model.dto.organization.DonationDTO;
import model.dto.organization.DonationReceiptDTO;

import java.util.List;

public interface OrganizationDonationService {

    List<DonationDTO> getDonationsByOrganization(Long organizationId);

    DonationDTO getDonationById(Long id);

    DonationDTO markDonationAsReceived(Long id, DonationReceiptDTO receiptDTO);

    List<DonationDTO> getPendingFeedbackDonations(Long organizationId);
}