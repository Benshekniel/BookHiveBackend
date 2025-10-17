package service.organization;

import model.dto.organization.BookRequestDTO;
import model.dto.organization.BookRequestCreateDTO;

import java.util.List;

public interface BookRequestService {

    BookRequestDTO createBookRequest(BookRequestCreateDTO createDTO);

    List<BookRequestDTO> getRequestsByOrganization(Long organizationId);

    BookRequestDTO getRequestById(Long id);

    BookRequestDTO updateRequest(Long id, BookRequestCreateDTO updateDTO);

    void cancelRequest(Long id);
}