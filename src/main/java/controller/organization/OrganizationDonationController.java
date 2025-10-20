package controller.organization;

import model.dto.organization.DonationDTO;
import model.dto.organization.DonationReceiptDTO;
import service.organization.OrganizationDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
@CrossOrigin(origins = {"http://localhost:9999", "http://localhost:3000"})

@RestController
@RequestMapping("/api/organization-donations")
public class OrganizationDonationController {

   private final OrganizationDonationService donationService;

   @Autowired
   public OrganizationDonationController(OrganizationDonationService donationService) {
       this.donationService = donationService;
   }

   @GetMapping("/organization/{organizationId}")
   public ResponseEntity<List<DonationDTO>> getDonationsByOrganization(@PathVariable Long organizationId) {
       List<DonationDTO> donations = donationService.getDonationsByOrganization(organizationId);
       return ResponseEntity.ok(donations);
   }

   @GetMapping("/{id}")
   public ResponseEntity<DonationDTO> getDonationById(@PathVariable Long id) {
       DonationDTO donation = donationService.getDonationById(id);
       return ResponseEntity.ok(donation);
   }

   @PostMapping("/{id}/mark-received")
   public ResponseEntity<DonationDTO> markDonationAsReceived(
           @PathVariable Long id,
           @Valid @RequestBody DonationReceiptDTO receiptDTO) {
       DonationDTO updated = donationService.markDonationAsReceived(id, receiptDTO);
       return ResponseEntity.ok(updated);
   }

   @GetMapping("/pending-feedback/{organizationId}")
   public ResponseEntity<List<DonationDTO>> getPendingFeedbackDonations(@PathVariable Long organizationId) {
       List<DonationDTO> pendingDonations = donationService.getPendingFeedbackDonations(organizationId);
       return ResponseEntity.ok(pendingDonations);
   }
}