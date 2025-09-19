// BookRequestServiceImpl.java
package service.organization.impl;

import model.dto.Organization.BookRequestDto.*;
import model.entity.BookRequest;
import model.entity.Organization;
import model.repo.organization.BookRequestRepository;
import model.repo.organization.OrganizationRepository;
import model.repo.organization.NotificationRepository;
import service.organization.BookRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookRequestServiceImpl implements BookRequestService {

    private final BookRequestRepository bookRequestRepository;
    private final OrganizationRepository organizationRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public BookRequestResponseDto createBookRequest(BookRequestCreateDto requestData) {
        log.info("Creating book request for organization ID: {}", requestData.getOrganizationId());
        
        // Validate organization exists
        Organization organization = organizationRepository.findById(requestData.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        // Create book request
        BookRequest bookRequest = new BookRequest();
        bookRequest.setOrganizationId(requestData.getOrganizationId());
        bookRequest.setTitle(requestData.getTitle());
        bookRequest.setDescription(requestData.getDescription());
        bookRequest.setQuantity(requestData.getQuantity());
        bookRequest.setCategories(requestData.getCategories());
        bookRequest.setStatus("PENDING");
        bookRequest.setPriority(requestData.getPriority());
        bookRequest.setCreatedAt(LocalDateTime.now());
        
        BookRequest savedRequest = bookRequestRepository.save(bookRequest);
        log.info("Created book request with ID: {}", savedRequest.getId());
        
        // Create notification for admin (in a real system)
        createNotification(
            null,  // Admin notification doesn't need an organization ID
            "New book request",
            "New book request created by " + organization.getName(),
            "BOOK_REQUEST",
            savedRequest.getId()
        );
        
        return convertToResponseDto(savedRequest);
    }

    @Override
    public List<BookRequestResponseDto> getBookRequestsByOrganization(Long orgId) {
        log.info("Fetching book requests for organization ID: {}", orgId);
        return bookRequestRepository.findByOrganizationIdOrderByCreatedAtDesc(orgId)
                .stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    @Override
    public Optional<BookRequestResponseDto> getBookRequestById(Long requestId) {
        log.info("Fetching book request with ID: {}", requestId);
        return bookRequestRepository.findById(requestId)
                .map(this::convertToResponseDto);
    }

    @Override
    public BookRequestResponseDto updateBookRequest(Long requestId, BookRequestUpdateDto updateData) {
        log.info("Updating book request with ID: {}", requestId);
        
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Book request not found"));
        
        // Update fields
        if (updateData.getTitle() != null) {
            bookRequest.setTitle(updateData.getTitle());
        }
        if (updateData.getDescription() != null) {
            bookRequest.setDescription(updateData.getDescription());
        }
        if (updateData.getQuantity() != null) {
            bookRequest.setQuantity(updateData.getQuantity());
        }
        if (updateData.getCategories() != null) {
            bookRequest.setCategories(updateData.getCategories());
        }
        if (updateData.getStatus() != null) {
            String oldStatus = bookRequest.getStatus();
            bookRequest.setStatus(updateData.getStatus());
            
            // If status changed to COMPLETED, set fulfilled date
            if ("COMPLETED".equals(updateData.getStatus()) && !"COMPLETED".equals(oldStatus)) {
                bookRequest.setFulfilledAt(LocalDateTime.now());
                
                // Create notification for organization
                createNotification(
                    bookRequest.getOrganizationId(),
                    "Request fulfilled",
                    "Your book request '" + bookRequest.getTitle() + "' has been fulfilled",
                    "BOOK_REQUEST_FULFILLED",
                    requestId
                );
            }
            
            // If status changed to REJECTED
            if ("REJECTED".equals(updateData.getStatus()) && !"REJECTED".equals(oldStatus)) {
                // Create notification for organization
                createNotification(
                    bookRequest.getOrganizationId(),
                    "Request rejected",
                    "Your book request '" + bookRequest.getTitle() + "' has been rejected",
                    "BOOK_REQUEST_REJECTED",
                    requestId
                );
            }
        }
        if (updateData.getNotes() != null) {
            bookRequest.setNotes(updateData.getNotes());
        }
        
        bookRequest.setUpdatedAt(LocalDateTime.now());
        BookRequest updatedRequest = bookRequestRepository.save(bookRequest);
        log.info("Updated book request with ID: {}", requestId);
        
        return convertToResponseDto(updatedRequest);
    }

    @Override
    public void cancelBookRequest(Long requestId) {
        log.info("Cancelling book request with ID: {}", requestId);
        
        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Book request not found"));
        
        // Only allow cancellation of pending or active requests
        if (!"PENDING".equals(bookRequest.getStatus()) && !"ACTIVE".equals(bookRequest.getStatus())) {
            throw new RuntimeException("Cannot cancel a request that is not pending or active");
        }
        
        bookRequestRepository.deleteById(requestId);
        log.info("Cancelled book request with ID: {}", requestId);
        
        // Create notification for admin
        createNotification(
            null,
            "Request cancelled",
            "Book request has been cancelled by the organization",
            "BOOK_REQUEST_CANCELLED",
            requestId
        );
    }
    
    // Helper methods
    private BookRequestResponseDto convertToResponseDto(BookRequest bookRequest) {
        BookRequestResponseDto dto = new BookRequestResponseDto();
        dto.setId(bookRequest.getId());
        dto.setOrganizationId(bookRequest.getOrganizationId());
        dto.setTitle(bookRequest.getTitle());
        dto.setDescription(bookRequest.getDescription());
        dto.setStatus(bookRequest.getStatus());
        dto.setQuantity(bookRequest.getQuantity());
        dto.setCategories(bookRequest.getCategories());
        dto.setCreatedAt(bookRequest.getCreatedAt());
        dto.setUpdatedAt(bookRequest.getUpdatedAt());
        dto.setFulfilledAt(bookRequest.getFulfilledAt());
        dto.setPriority(bookRequest.getPriority());
        dto.setNotes(bookRequest.getNotes());
        
        // Get organization name
        organizationRepository.findById(bookRequest.getOrganizationId())
                .ifPresent(org -> dto.setOrganizationName(org.getName()));
        
        return dto;
    }
    
    private void createNotification(Long organizationId, String title, String message, String type, Long referenceId) {
        try {
            // This is a placeholder implementation - replace with your actual notification logic
            if (notificationRepository != null) {
                notificationRepository.createNotification(organizationId, title, message, type, referenceId);
            }
        } catch (Exception e) {
            log.error("Failed to create notification", e);
        }
    }
}