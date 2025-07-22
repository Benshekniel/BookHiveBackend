package controller;

import lombok.RequiredArgsConstructor;
import model.dto.BookStore.BookDTO.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BookStore.BookService;

//import java.util.List;
//import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:9999")
public class BookController {

    private final BookService bookService;

    // Create single book
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookCreateDTO createDTO) {
        BookResponseDTO createdBook = BookResponseDTO.fromEntity(bookService.createBook(createDTO));
        return ResponseEntity.ok(createdBook);
    }

//    // Get all books
//    @GetMapping
//    public ResponseEntity<List<BookListDTO>> getAllBooks() {
//        List<BookListDTO> books = bookService.getAllBooks();
//        return ResponseEntity.ok(books);
//    }
//
//    // Optional: get books for dashboard (alias for all books)
//    @GetMapping("/my-books")
//    public ResponseEntity<List<BookListDTO>> getMyBooks() {
//        List<BookListDTO> books = bookService.getAllBooks(); // Same as above
//        return ResponseEntity.ok(books);
//    }
//
//    // Get single book by ID
//    @GetMapping("/{bookId}")
//    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Integer bookId) {
//        Optional<BookResponseDTO> book = bookService.getBookById(bookId);
//        return book.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

    // Update book
    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Integer bookId,
            @RequestBody BookUpdateDTO updateDTO) {

        updateDTO.setBookId(bookId); // Ensure ID is set before updating

        try {
            BookResponseDTO updatedBook = BookResponseDTO.fromEntity(bookService.updateBook(bookId, updateDTO));
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete book
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer bookId) {
        try {
            bookService.deleteBook(bookId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    // Bulk create books
//    @PostMapping("/bulk")
//    public ResponseEntity<List<BookResponseDTO>> bulkCreateBooks(@RequestBody BookBulkCreateDTO bulkCreateDTO) {
//        List<BookResponseDTO> createdBooks = bookService.bulkCreateBooks(bulkCreateDTO);
//        return ResponseEntity.ok(createdBooks);
//    }
//
//    // Search books
//    @GetMapping("/search")
//    public ResponseEntity<List<BookListDTO>> searchBooks(@RequestParam String query) {
//        List<BookListDTO> books = bookService.searchBooks(query);
//        return ResponseEntity.ok(books);
//    }
}
