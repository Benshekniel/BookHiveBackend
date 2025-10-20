package service.organization.impl;

import model.dto.organization.BookRequestDTO;
import model.dto.organization.BookRequestCreateDTO;
import model.entity.BookRequest;
import model.entity.Donation;
import model.entity.Organization;
import model.repo.OrgRepo;
import model.repo.organization.BookRequestRepository;
import model.repo.organization.DonationRepository;
import service.organization.BookRequestService;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookRequestServiceImpl implements BookRequestService {

    private final BookRequestRepository bookRequestRepository;
    private final DonationRepository donationRepository;
    private final OrgRepo orgRepo;

    public BookRequestServiceImpl(
            BookRequestRepository bookRequestRepository,
            DonationRepository donationRepository,
            OrgRepo orgRepo) {
        this.bookRequestRepository = bookRequestRepository;
        this.donationRepository = donationRepository;
        this.orgRepo = orgRepo;
    }

    @Override
    public BookRequestDTO createBookRequest(BookRequestCreateDTO createDTO) {
        // Find organization
        Organization organization = orgRepo.findByOrgId(createDTO.getOrganizationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found with orgId: " + createDTO.getOrganizationId()));

        // Create request entity
        BookRequest request = new BookRequest();
        request.setOrganization(organization); // Reverted to use setOrganization
        request.setTitle(createDTO.getTitle());
        request.setSubject(createDTO.getSubject());
        request.setQuantity(createDTO.getQuantity());
        request.setUrgency(createDTO.getUrgency());
        request.setDescription(createDTO.getDescription());
        request.setStatus("PENDING");
        request.setDateRequested(LocalDateTime.now()); // Reverted to use dateRequested

        // Save to book_requests table
        BookRequest saved = bookRequestRepository.save(request);

        // Also save to donations table
        Donation donation = new Donation();
        donation.setOrganizationId(createDTO.getOrganizationId());
        donation.setDonorId(0L); // No donor yet, set to 0 or null if allowed
        donation.setBookTitle(createDTO.getTitle());
        donation.setQuantity(createDTO.getQuantity());
        donation.setQuantityCurrent(null); // Keep NULL for book requests
        donation.setStatus("PENDING"); // Start as PENDING
        donation.setCategory(createDTO.getSubject()); // Use subject as category
        donation.setNotes(createDTO.getDescription());
        donation.setPriority(mapUrgencyToPriority(createDTO.getUrgency())); // Map urgency to priority
        donation.setDateDonated(LocalDateTime.now());
        donation.setBookRequestId(saved.getId()); // Link to book request
        
        // Save to donations table
        donationRepository.save(donation);

        return mapToDTO(saved);
    }

    // Helper method to map urgency to priority
    private String mapUrgencyToPriority(String urgency) {
        if (urgency == null) {
            return "Medium";
        }
        switch (urgency.toLowerCase()) {
            case "high":
            case "urgent":
                return "High";
            case "low":
                return "Low";
            case "medium":
            default:
                return "Medium";
        }
    }

    @Override
    public List<BookRequestDTO> getRequestsByOrganization(Long orgId) {
        // Ensure organization exists
        if (!orgRepo.existsByOrgId(orgId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found with orgId: " + orgId);
        }

        // Get requests and map to DTOs
        List<BookRequest> requests = bookRequestRepository.findByOrganizationOrgIdOrderByDateRequestedDesc(orgId);
        return requests.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookRequestDTO getRequestById(Long id) {
        BookRequest request = bookRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book request not found with id: " + id));
        return mapToDTO(request);
    }

    @Override
    public BookRequestDTO updateRequest(Long id, BookRequestCreateDTO updateDTO) {
        // Find request
        BookRequest request = bookRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book request not found with id: " + id));

        // Check if request is in an editable state
        if (!isRequestEditable(request.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This request cannot be edited in its current state");
        }

        // Update fields
        request.setTitle(updateDTO.getTitle());
        request.setSubject(updateDTO.getSubject());
        request.setQuantity(updateDTO.getQuantity());
        request.setUrgency(updateDTO.getUrgency());
        request.setDescription(updateDTO.getDescription());

        // Save book request
        BookRequest updated = bookRequestRepository.save(request);

        // Also update the corresponding donation record
        updateDonationFromBookRequest(updated);

        return mapToDTO(updated);
    }

    @Override
    public void cancelRequest(Long id) {
        // Find request
        BookRequest request = bookRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book request not found with id: " + id));

        // Check if request can be deleted
        if (!isRequestCancelable(request.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This request cannot be deleted in its current state");
        }

        // Delete corresponding donation record first
        deleteDonationByBookRequestId(id);

        // Then delete the request from database
        bookRequestRepository.delete(request);
    }

    // Helper method to update donation when book request is updated
    private void updateDonationFromBookRequest(BookRequest request) {
        // Find donation by book request ID using more efficient method
        Optional<Donation> donationOpt = donationRepository.findByBookRequestId(request.getId());

        if (donationOpt.isPresent()) {
            Donation donation = donationOpt.get();
            // Update donation fields
            donation.setBookTitle(request.getTitle());
            donation.setQuantity(request.getQuantity());
            donation.setQuantityCurrent(null); // Keep NULL for book requests
            donation.setCategory(request.getSubject());
            donation.setNotes(request.getDescription());
            donation.setPriority(mapUrgencyToPriority(request.getUrgency()));
            donation.setStatus(request.getStatus());
            
            // Save updated donation
            donationRepository.save(donation);
        }
    }

    // Helper method to delete donation when book request is deleted
    private void deleteDonationByBookRequestId(Long bookRequestId) {
        // Use the repository's delete method
        donationRepository.deleteByBookRequestId(bookRequestId);
    }

    private boolean isRequestEditable(String status) {
        return "PENDING".equals(status) || "DRAFT".equals(status);
    }

    private boolean isRequestCancelable(String status) {
        return "PENDING".equals(status) || "DRAFT".equals(status) || "APPROVED".equals(status);
    }

    private BookRequestDTO mapToDTO(BookRequest request) {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setId(request.getId());
        dto.setOrganizationId(request.getOrganization().getOrgId()); // Use organization relationship
        dto.setTitle(request.getTitle());
        dto.setSubject(request.getSubject());
        dto.setQuantity(request.getQuantity());
        dto.setUrgency(request.getUrgency());
        dto.setDescription(request.getDescription());
        dto.setStatus(request.getStatus());
        dto.setDateRequested(request.getDateRequested().toString()); // Use dateRequested

        if (request.getDateApproved() != null) {
            dto.setDateApproved(request.getDateApproved().toString());
        }

        if (request.getDateFulfilled() != null) {
            dto.setDateFulfilled(request.getDateFulfilled().toString());
        }

        return dto;
    }
}