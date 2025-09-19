// 3. BookRequestController.java
package controller.organization;

import model.dto.Organization.BookRequestDto.*;
import service.organization.impl.BookRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/book-requests")
@RequiredArgsConstructor
public class BookRequestController {

    private final BookRequestServiceImpl bookRequestService;

    @PostMapping
    public ResponseEntity<BookRequestResponseDto> createBookRequest(@RequestBody BookRequestCreateDto requestData) {
        try {
            BookRequestResponseDto response = bookRequestService.createBookRequest(requestData);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<BookRequestResponseDto>> getBookRequestsByOrganization(@PathVariable Long orgId) {
        List<BookRequestResponseDto> requests = bookRequestService.getBookRequestsByOrganization(orgId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<BookRequestResponseDto> getBookRequestById(@PathVariable Long requestId) {
        return bookRequestService.getBookRequestById(requestId)
                .map(request -> ResponseEntity.ok(request))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<BookRequestResponseDto> updateBookRequest(
            @PathVariable Long requestId,
            @RequestBody BookRequestUpdateDto updateData) {
        try {
            BookRequestResponseDto response = bookRequestService.updateBookRequest(requestId, updateData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> cancelBookRequest(@PathVariable Long requestId) {
        try {
            bookRequestService.cancelBookRequest(requestId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}