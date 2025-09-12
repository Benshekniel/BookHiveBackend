package service.organization;

import model.dto.Organization.BookRequestDto.*;
import java.util.List;
import java.util.Optional;

public interface BookRequestService {
    
    // Create a new book request
    BookRequestResponseDto createBookRequest(BookRequestCreateDto requestData);
    
    // Get all book requests for an organization
    List<BookRequestResponseDto> getBookRequestsByOrganization(Long orgId);
    
    // Get book request by ID
    Optional<BookRequestResponseDto> getBookRequestById(Long requestId);
    
    // Update book request
    BookRequestResponseDto updateBookRequest(Long requestId, BookRequestUpdateDto updateData);
    
    // Cancel book request
    void cancelBookRequest(Long requestId);
}