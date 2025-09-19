// 4. OrganizationDonationController.java
package controller.organization;

import model.dto.Organization.DonationDto.*;
import service.organization.impl.DonationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/organization-donations")
@RequiredArgsConstructor
public class OrganizationDonationController {

    private final DonationServiceImpl donationService;

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<DonationResponseDto>> getDonationsByOrganization(@PathVariable Long orgId) {
        List<DonationResponseDto> donations = donationService.getDonationsByOrganization(orgId);
        return ResponseEntity.ok(donations);
    }

    @PostMapping("/{donationId}/mark-received")
    public ResponseEntity<DonationResponseDto> markDonationAsReceived(
            @PathVariable Long donationId,
            @RequestBody DonationConfirmationDto confirmationData) {
        try {
            DonationResponseDto response = donationService.markDonationAsReceived(donationId, confirmationData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/pending-feedback/{orgId}")
    public ResponseEntity<List<PendingDonationDto>> getPendingDonations(@PathVariable Long orgId) {
        try {
            List<PendingDonationDto> pendingDonations = donationService.getPendingDonations(orgId);
            return ResponseEntity.ok(pendingDonations);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of()); // Return empty list on error as per JS implementation
        }
    }
}