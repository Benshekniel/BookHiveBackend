package controller.BookStore;

import model.dto.BookStore.BSBookDTOs.RegisterBookDTO;
import model.dto.BookStore.BSBookDTOs.UpdateBookDTO;
import model.dto.BookStore.BSBookDTOs.ViewBookDTO;
import org.springframework.http.HttpStatus;
import service.BookStore.BSBookService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import service.BookStore.BookStoreService;

import java.util.List;

@RestController
@RequestMapping("/api/bs-book")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BSBookController {

    private final BSBookService bookService;

    @PostMapping
    public ResponseEntity<String> registerBook ( @RequestBody RegisterBookDTO regBookDTO ) {
        Integer userId = regBookDTO.getUserId();

        boolean saved = bookService.registerBook(regBookDTO, userId);

        if (saved) return ResponseEntity.ok("Book(s) added successfully");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book(s) could not be added!");
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<String> updateBook (
            @PathVariable("bookId") Integer bookId,
            @RequestBody UpdateBookDTO bookDTO ) {

        boolean updated = bookService.updateBook(bookId, bookDTO);
        if (updated) return ResponseEntity.ok("Book updated successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book Not found!");
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ViewBookDTO> getBook ( @PathVariable("bookId") Integer bookId ) {

        ViewBookDTO formattedBook = bookService.getBookById(bookId);
        if (formattedBook == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(formattedBook);
    }

//    @GetMapping("/store/{storeId}")
//    public ResponseEntity<List<ViewBookDTO>> getBooksByStoreId (
//            @PathVariable("storeId") Integer storeId ) {
//        return bookService.getBooksByStore(storeId);
//    }

}
