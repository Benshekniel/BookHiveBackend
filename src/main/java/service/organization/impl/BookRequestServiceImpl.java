//package service.organization.impl;
//
//import model.dto.organization.BookRequestDTO;
//import model.dto.organization.BookRequestCreateDTO;
//import model.entity.BookRequest;
//import model.entity.Organization;
//import model.repo.organization.BookRequestRepository;
//import model.repo.organization.OrganizationRepository;
//import service.organization.BookRequestService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class BookRequestServiceImpl implements BookRequestService {
//
//    private final BookRequestRepository bookRequestRepository;
//    private final OrganizationRepository organizationRepository;
//
//    @Autowired
//    public BookRequestServiceImpl(
//            BookRequestRepository bookRequestRepository,
//            OrganizationRepository organizationRepository) {
//        this.bookRequestRepository = bookRequestRepository;
//        this.organizationRepository = organizationRepository;
//    }
//
//    @Override
//    public BookRequestDTO createBookRequest(BookRequestCreateDTO createDTO) {
//        // Find organization
//        Organization organization = organizationRepository.findById(createDTO.getOrganizationId())
//                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
//
//        // Create request entity
//        BookRequest request = new BookRequest();
//        request.setOrganization(organization);
//        request.setTitle(createDTO.getTitle());
//        request.setSubject(createDTO.getSubject());
//        request.setQuantity(createDTO.getQuantity());
//        request.setUrgency(createDTO.getUrgency());
//        request.setDescription(createDTO.getDescription());
//        request.setStatus("PENDING");
//        request.setDateRequested(LocalDateTime.now());
//
//        // Save and return mapped DTO
//        BookRequest saved = bookRequestRepository.save(request);
//        return mapToDTO(saved);
//    }
//
//    @Override
//    public List<BookRequestDTO> getRequestsByOrganization(Long organizationId) {
//        // Ensure organization exists
//        if (!organizationRepository.existsById(organizationId)) {
//            throw new ResourceNotFoundException("Organization not found");
//        }
//
//        // Get requests and map to DTOs
//        List<BookRequest> requests = bookRequestRepository.findByOrganizationIdOrderByDateRequestedDesc(organizationId);
//        return requests.stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public BookRequestDTO getRequestById(Long id) {
//        BookRequest request = bookRequestRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Book request not found"));
//        return mapToDTO(request);
//    }
//
//    @Override
//    public BookRequestDTO updateRequest(Long id, BookRequestCreateDTO updateDTO) {
//        // Find request
//        BookRequest request = bookRequestRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Book request not found"));
//
//        // Check if request is in an editable state
//        if (!isRequestEditable(request.getStatus())) {
//            throw new BadRequestException("This request cannot be edited in its current state");
//        }
//
//        // Update fields
//        request.setTitle(updateDTO.getTitle());
//        request.setSubject(updateDTO.getSubject());
//        request.setQuantity(updateDTO.getQuantity());
//        request.setUrgency(updateDTO.getUrgency());
//        request.setDescription(updateDTO.getDescription());
//
//        // Save and return
//        BookRequest updated = bookRequestRepository.save(request);
//        return mapToDTO(updated);
//    }
//
//    @Override
//    public void cancelRequest(Long id) {
//        // Find request
//        BookRequest request = bookRequestRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Book request not found"));
//
//        // Check if request can be canceled
//        if (!isRequestCancelable(request.getStatus())) {
//            throw new BadRequestException("This request cannot be canceled in its current state");
//        }
//
//        // Update status to canceled
//        request.setStatus("CANCELED");
//        bookRequestRepository.save(request);
//    }
//
//    private boolean isRequestEditable(String status) {
//        return "PENDING".equals(status) || "DRAFT".equals(status);
//    }
//
//    private boolean isRequestCancelable(String status) {
//        return "PENDING".equals(status) || "DRAFT".equals(status) || "APPROVED".equals(status);
//    }
//
//    private BookRequestDTO mapToDTO(BookRequest request) {
//        BookRequestDTO dto = new BookRequestDTO();
//        dto.setId(request.getId());
//        dto.setOrganizationId(request.getOrganization().getId());
//        dto.setTitle(request.getTitle());
//        dto.setSubject(request.getSubject());
//        dto.setQuantity(request.getQuantity());
//        dto.setUrgency(request.getUrgency());
//        dto.setDescription(request.getDescription());
//        dto.setStatus(request.getStatus());
//        dto.setDateRequested(request.getDateRequested().toString());
//
//        if (request.getDateApproved() != null) {
//            dto.setDateApproved(request.getDateApproved().toString());
//        }
//
//        if (request.getDateFulfilled() != null) {
//            dto.setDateFulfilled(request.getDateFulfilled().toString());
//        }
//
//        return dto;
//    }
//}