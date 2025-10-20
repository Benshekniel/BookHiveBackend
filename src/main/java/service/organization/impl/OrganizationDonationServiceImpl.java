package service.organization.impl;

import model.dto.organization.DonationDTO;
import model.dto.organization.DonationReceiptDTO;
import model.entity.Donation;
import model.repo.OrgRepo;
import model.repo.organization.DonationRepository;
import service.organization.OrganizationDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationDonationServiceImpl implements OrganizationDonationService {

    private final DonationRepository donationRepository;
    private final OrgRepo orgRepo;

    @Autowired
    public OrganizationDonationServiceImpl(
            DonationRepository donationRepository,
            OrgRepo orgRepo) {
        this.donationRepository = donationRepository;
        this.orgRepo = orgRepo;
    }

    @Override
    public List<DonationDTO> getDonationsByOrganization(Long orgId) {
        // Ensure organization exists
        if (!orgRepo.existsByOrgId(orgId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found");
        }

        // Get donations and map to DTOs
        List<Donation> donations = donationRepository.findByOrganizationIdOrderByDateDonatedDesc(orgId);
        return donations.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DonationDTO getDonationById(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Donation not found"));
        return mapToDTO(donation);
    }

    @Override
    public DonationDTO markDonationAsReceived(Long id, DonationReceiptDTO receiptDTO) {
        // Find donation
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Donation not found"));

        // Verify organization using organizationId
        if (!donation.getOrganizationId().equals(receiptDTO.getOrganizationId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Organization ID mismatch");
        }

        // Verify donation status
        if (!"SHIPPED".equals(donation.getStatus()) && !"IN_TRANSIT".equals(donation.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Donation is not in a shippable state");
        }

        // Update donation
        donation.setStatus("RECEIVED");
        try {
            donation.setDateReceived(LocalDate.parse(receiptDTO.getReceivedDate()));
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid received date format");
        }
        // Note: Condition field is not in Donation.java, so it's omitted
        if (receiptDTO.getNotes() != null && !receiptDTO.getNotes().isEmpty()) {
            donation.setNotes(receiptDTO.getNotes());
        }

        // Save and return
        Donation updated = donationRepository.save(donation);
        return mapToDTO(updated);
    }

    @Override
    public List<DonationDTO> getPendingFeedbackDonations(Long orgId) {
        // Ensure organization exists
        if (!orgRepo.existsByOrgId(orgId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found");
        }

        // Get donations that are received but haven't received feedback
        List<Donation> pendingDonations = donationRepository.findByOrganizationIdAndStatusAndNoFeedback(orgId, "RECEIVED");
        return pendingDonations.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private DonationDTO mapToDTO(Donation donation) {
        DonationDTO dto = new DonationDTO();
        dto.setId(donation.getId());
        dto.setOrganizationId(donation.getOrganizationId());
        dto.setDonorId(donation.getDonorId());
        // Note: donorName and donorLocation are not in Donation.java
        // If needed, fetch from a Donor entity using donorId (requires DonorRepository)
        dto.setDonorName(null); // Replace with donorRepository.findById(donation.getDonorId()).getName() if available
        dto.setDonorLocation(null); // Replace with donorRepository.findById(donation.getDonorId()).getLocation() if available
        dto.setBookTitle(donation.getBookTitle());
        dto.setQuantity(donation.getQuantity());
        dto.setQuantityCurrent(donation.getQuantityCurrent());
        // Note: condition field is not in Donation.java
        dto.setCondition(null); // Set to null or a default value
        dto.setStatus(donation.getStatus());
        dto.setTrackingNumber(donation.getTrackingNumber());
        dto.setNotes(donation.getNotes());
        dto.setPriority(donation.getPriority());

        // Handle nullable date fields
        if (donation.getDateDonated() != null) {
            dto.setDonationDate(donation.getDateDonated().toString());
        } else {
            dto.setDonationDate(null);
        }

        if (donation.getDateShipped() != null) {
            dto.setDateShipped(donation.getDateShipped().toString());
        } else {
            dto.setDateShipped(null);
        }

        if (donation.getEstimatedDelivery() != null) {
            dto.setEstimatedDelivery(donation.getEstimatedDelivery().toString());
        } else {
            dto.setEstimatedDelivery(null);
        }

        if (donation.getDateReceived() != null) {
            dto.setDateReceived(donation.getDateReceived().toString());
        } else {
            dto.setDateReceived(null);
        }

        return dto;
    }
}