package service.organization.impl;

import model.dto.organization.BookRequestCreateDTO;
import model.dto.organization.BookRequestResponseDTO;
import model.dto.organization.BookRequestUpdateDTO;
import model.entity.BookRequest;
import model.repo.BookRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.organization.BookRequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookRequestServiceImpl implements BookRequestService {

    @Autowired
    private BookRequestRepository bookRequestRepository;

    private BookRequestResponseDTO mapToDTO(BookRequest entity) {
        return new BookRequestResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getQuantity(),
                entity.getStatus(),
                entity.getRequestedAt()
        );
    }

    @Override
    public BookRequestResponseDTO createBookRequest(BookRequestCreateDTO dto) {
        BookRequest entity = new BookRequest(
                dto.getTitle(),
                dto.getQuantity(),
                "Pending",
                LocalDateTime.now(),
                dto.getOrganizationId()
        );
        bookRequestRepository.save(entity);
        return mapToDTO(entity);
    }

    @Override
    public List<BookRequestResponseDTO> getBookRequestsByOrganization(Long organizationId) {
        return bookRequestRepository.findByOrganizationId(organizationId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public BookRequestResponseDTO updateBookRequestStatus(Long requestId, BookRequestUpdateDTO dto) {
        BookRequest entity = bookRequestRepository.findById(requestId).orElse(null);
        if (entity == null) return null;
        entity.setStatus(dto.getStatus());
        bookRequestRepository.save(entity);
        return mapToDTO(entity);
    }

    @Override
    public void deleteBookRequest(Long requestId) {
        bookRequestRepository.deleteById(requestId);
    }
}