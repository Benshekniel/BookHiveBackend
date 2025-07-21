//package controller;
//
//import model.dto.BookStore.BookDTO.*;
//import service.BookService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/books")
//@CrossOrigin(origins = "http://localhost:9999")
//public class BookController {
//
//    @Autowired
//    private BookService bookService;
//
//    // Create single book
//    @PostMapping
//    public ResponseEntity<BookResponseDTO> createBook(
//            @RequestBody BookCreateDTO createDTO,
//            Authentication auth) {
//
//        Long userId = getUserIdFromAuth(auth);
//        BookResponseDTO createdBook = bookService.createBook(createDTO, userId);
//        return ResponseEntity.ok(createdBook);
//    }
//
//    // Get all books with pagination
//    @GetMapping
//    public ResponseEntity<Page<BookListDTO>> getAllBooks(Pageable pageable) {
//        Page<BookListDTO> books = bookService.getAllBooks(pageable);
//        return ResponseEntity.ok(books);
//    }
//
//    // Get books by current user (bookstore dashboard)
//    @GetMapping("/my-books")
//    public ResponseEntity<List<BookListDTO>> getMyBooks(Authentication auth) {
//        Long userId = getUserIdFromAuth(auth);
//        List<BookListDTO> books = bookService.getBooksByUser(userId);
//        return ResponseEntity.ok(books);
//    }
//
//    // Get single book by ID
//    @GetMapping("/{bookId}")
//    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long bookId) {
//        Optional<BookResponseDTO> book = bookService.getBookById(bookId);
//        return book.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    // Update book
//    @PutMapping("/{bookId}")
//    public ResponseEntity<BookResponseDTO> updateBook(
//            @PathVariable Long bookId,
//            @RequestBody BookUpdateDTO updateDTO,
//            Authentication auth) {
//
//        updateDTO.setBookId(bookId);  // Ensure ID consistency
//        Long userId = getUserIdFromAuth(auth);
//
//        try {
//            BookResponseDTO updatedBook = bookService.updateBook(updateDTO, userId);
//            return ResponseEntity.ok(updatedBook);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    // Delete book
//    @DeleteMapping("/{bookId}")
//    public ResponseEntity<Void> deleteBook(
//            @PathVariable Long bookId,
//            Authentication auth) {
//
//        Long userId = getUserIdFromAuth(auth);
//
//        try {
//            bookService.deleteBook(bookId, userId);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    // Bulk create books (for bookstores)
//    @PostMapping("/bulk")
//    public ResponseEntity<List<BookResponseDTO>> bulkCreateBooks(
//            @RequestBody BookBulkCreateDTO bulkCreateDTO,
//            Authentication auth) {
//
//        Long userId = getUserIdFromAuth(auth);
//        List<BookResponseDTO> createdBooks = bookService.bulkCreateBooks(bulkCreateDTO, userId);
//        return ResponseEntity.ok(createdBooks);
//    }
//
//    // Search books
//    @GetMapping("/search")
//    public ResponseEntity<List<BookListDTO>> searchBooks(@RequestParam String query) {
//        List<BookListDTO> books = bookService.searchBooks(query);
//        return ResponseEntity.ok(books);
//    }
//
//    // Helper method to extract user ID from authentication
//    private Long getUserIdFromAuth(Authentication auth) {
//        // This depends on your authentication implementation
//        // Assuming you have a custom User principal with getId() method
//        return ((CustomUserPrincipal) auth.getPrincipal()).getId();
//    }
//}