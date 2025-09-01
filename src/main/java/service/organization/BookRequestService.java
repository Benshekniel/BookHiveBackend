package service.organization;

import model.dto.organization.BookRequestCreateDTO;
import model.dto.organization.BookRequestResponseDTO;
import model.dto.organization.BookRequestUpdateDTO;

import java.util.List;

public interface BookRequestService {
    BookRequestResponseDTO createBookRequest(BookRequestCreateDTO dto);
    List<BookRequestResponseDTO> getBookRequestsByOrganization(Long organizationId);
    BookRequestResponseDTO updateBookRequestStatus(Long requestId, BookRequestUpdateDTO dto);
    void deleteBookRequest(Long requestId);
}