//package service.organization.impl;
//
//
//import model.dto.organization.DonationDTO;
//import model.dto.organization.DonationReceiptDTO;
//import model.entity.Donation;
//import model.repo.organization.DonationRepository;
//import model.repo.organization.OrganizationRepository;
//import service.organization.OrganizationDonationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class OrganizationDonationServiceImpl implements OrganizationDonationService {
//
//    private final DonationRepository donationRepository;
//    private final OrganizationRepository organizationRepository;
//
//    @Autowired
//    public OrganizationDonationServiceImpl(
//            DonationRepository donationRepository,
//            OrganizationRepository organizationRepository) {
//        this.donationRepository = donationRepository;
//        this.organizationRepository = organizationRepository;
//    }
//
//    @Override
//    public List<DonationDTO> getDonationsByOrganization(Long organizationId) {
//        // Ensure organization exists
//        if (!organizationRepository.existsById(organizationId)) {
//            throw new ResourceNotFoundException("Organization not found");
//        }
//
//        // Get donations and map to DTOs
//        List<Donation> donations = donationRepository.findByOrganizationIdOrderByDateDonatedDesc(organizationId);
//        return donations.stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public DonationDTO getDonationById(Long id) {
//        Donation donation = donationRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));
//        return mapToDTO(donation);
//    }
//
//    @Override
//    public DonationDTO markDonationAsReceived(Long id, DonationReceiptDTO receiptDTO) {
//        // Find donation
//        Donation donation = donationRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));
//
//        // Verify organization
//        if (!donation.getOrganization().getId().equals(receiptDTO.getOrganizationId())) {
//            throw new BadRequestException("Organization ID mismatch");
//        }
//
//        // Verify donation status
//        if (!"SHIPPED".equals(donation.getStatus()) && !"IN_TRANSIT".equals(donation.getStatus())) {
//            throw new BadRequestException("Donation is not in a shippable state");
//        }
//
//        // Update donation
//        donation.setStatus("RECEIVED");
//        donation.setDateReceived(LocalDate.parse(receiptDTO.getReceivedDate()));
//        donation.setCondition(receiptDTO.getCondition());
//
//        if (receiptDTO.getNotes() != null && !receiptDTO.getNotes().isEmpty()) {
//            donation.setNotes(receiptDTO.getNotes());
//        }
//
//        // Save and return
//        Donation updated = donationRepository.save(donation);
//        return mapToDTO(updated);
//    }
//
//    @Override
//    public List<DonationDTO> getPendingFeedbackDonations(Long organizationId) {
//        // Get donations that are received but haven't received feedback
//        List<Donation> pendingDonations = donationRepository.findByOrganizationIdAndStatusAndFeedbackIsNull(
//                organizationId, "RECEIVED");
//        return pendingDonations.stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }
//
//    private DonationDTO mapToDTO(Donation donation) {
//        DonationDTO dto = new DonationDTO();
//        dto.setId(donation.getId());
//        dto.setOrganizationId(donation.getOrganization().getId());
//        dto.setDonorId(donation.getDonorId());
//        dto.setDonorName(donation.getDonorName());
//        dto.setDonorLocation(donation.getDonorLocation());
//        dto.setBookTitle(donation.getBookTitle());
//        dto.setQuantity(donation.getQuantity());
//        dto.setCondition(donation.getCondition());
//        dto.setStatus(donation.getStatus());
//        dto.setTrackingNumber(donation.getTrackingNumber());
//        dto.setNotes(donation.getNotes());
//
//        dto.setDonationDate(donation.getDateDonated().toString());
//
//        if (donation.getDateShipped() != null) {
//            dto.setDateShipped(donation.getDateShipped().toString());
//        }
//
//        if (donation.getEstimatedDelivery() != null) {
//            dto.setEstimatedDelivery(donation.getEstimatedDelivery().toString());
//        }
//
//        if (donation.getDateReceived() != null) {
//            dto.setDateReceived(donation.getDateReceived().toString());
//        }
//
//        return dto;
//    }
//}