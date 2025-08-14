package controller;

import model.dto.BookDTOs.RegisterBookDTO;
import model.dto.BookDTOs.UpdateBookDTO;
import model.dto.BookDTOs.ViewBookDTO;
import service.BookStore.BookService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookstore")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<String> registerBook (
            @RequestBody RegisterBookDTO bookDTO) {
        // ownerId will need to be taken from the logged in user's userId
        Integer ownerID = 603;
        return bookService.registerBook(bookDTO, ownerID);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<ViewBookDTO> getBook (
            @PathVariable("bookId") Integer bookId) {
        return bookService.getBookById(bookId);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<String> updateBook (
            @PathVariable("bookId") Integer bookId,
            @RequestBody UpdateBookDTO bookDTO) {

        return bookService.updateBook(bookId, bookDTO);
    }
}
