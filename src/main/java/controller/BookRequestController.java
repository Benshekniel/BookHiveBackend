package controller;

import model.dto.organization.BookRequestCreateDTO;
import model.dto.organization.BookRequestResponseDTO;
import model.dto.organization.BookRequestUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.organization.BookRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/book-requests")
public class BookRequestController {

    @Autowired
    private BookRequestService bookRequestService;

    @PostMapping
    public BookRequestResponseDTO createBookRequest(@RequestBody BookRequestCreateDTO dto) {
        return bookRequestService.createBookRequest(dto);
    }

    @GetMapping("/organization/{orgId}")
    public List<BookRequestResponseDTO> getBookRequestsByOrganization(@PathVariable Long orgId) {
        return bookRequestService.getBookRequestsByOrganization(orgId);
    }

    @PutMapping("/{requestId}")
    public BookRequestResponseDTO updateBookRequestStatus(@PathVariable Long requestId, @RequestBody BookRequestUpdateDTO dto) {
        return bookRequestService.updateBookRequestStatus(requestId, dto);
    }

    @DeleteMapping("/{requestId}")
    public void deleteBookRequest(@PathVariable Long requestId) {
        bookRequestService.deleteBookRequest(requestId);
    }
}