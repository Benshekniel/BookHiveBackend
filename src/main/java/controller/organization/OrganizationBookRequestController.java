//package controller.organization;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.validation.Valid;
//import model.dto.organization.BookRequestCreateDTO;
//import model.dto.organization.BookRequestDTO;
//import service.organization.BookRequestService;
//
//import java.util.List;
//@CrossOrigin(origins = {"http://localhost:9999", "http://localhost:3000"})
//
//@RestController
//@RequestMapping("/api/book-requests")
//public class OrganizationBookRequestController {
//
//    private final BookRequestService bookRequestService;
//
//    @Autowired
//    public OrganizationBookRequestController(BookRequestService bookRequestService) {
//        this.bookRequestService = bookRequestService;
//    }
//
//    @PostMapping
//    public ResponseEntity<BookRequestDTO> createBookRequest(@Valid @RequestBody BookRequestCreateDTO createDTO) {
//        BookRequestDTO created = bookRequestService.createBookRequest(createDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }
//
//    @GetMapping("/organization/{organizationId}")
//    public ResponseEntity<List<BookRequestDTO>> getRequestsByOrganization(@PathVariable Long organizationId) {
//        List<BookRequestDTO> requests = bookRequestService.getRequestsByOrganization(organizationId);
//        return ResponseEntity.ok(requests);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<BookRequestDTO> getRequestById(@PathVariable Long id) {
//        BookRequestDTO request = bookRequestService.getRequestById(id);
//        return ResponseEntity.ok(request);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<BookRequestDTO> updateRequest(
//            @PathVariable Long id,
//            @Valid @RequestBody BookRequestCreateDTO updateDTO) {
//        BookRequestDTO updated = bookRequestService.updateRequest(id, updateDTO);
//        return ResponseEntity.ok(updated);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> cancelRequest(@PathVariable Long id) {
//        bookRequestService.cancelRequest(id);
//        return ResponseEntity.ok().build();
//    }
//}