package controller.organization;

import model.dto.organization.DonationReceivedDTO;
import model.dto.organization.DonationConfirmationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.organization.DonationService;

import java.util.List;

@RestController
@RequestMapping("/api/organization-donations")
public class OrganizationDonationsController {

    @Autowired
    private DonationService donationService;

    @GetMapping("/organization/{orgId}")
    public List<DonationReceivedDTO> getDonationsByOrganization(@PathVariable Long orgId) {
        return donationService.getDonationsByOrganization(orgId);
    }

    @PostMapping("/{donationId}/mark-received")
    public DonationReceivedDTO markAsReceived(@PathVariable Long donationId, @RequestBody DonationConfirmationDTO dto) {
        return donationService.markAsReceived(donationId, dto);
    }
}
