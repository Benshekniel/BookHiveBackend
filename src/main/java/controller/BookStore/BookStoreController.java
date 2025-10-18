package controller.BookStore;

import model.dto.BookStore.BookStoreDTOs.RegisterBookStoreDTO;
import model.dto.BookStore.BookStoreDTOs.ProfileBookStoreDTO;
import org.springframework.http.HttpStatus;
import service.BookStore.BookStoreService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookstore")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BookStoreController {

    private final BookStoreService bookStoreService;

    @PostMapping
    public ResponseEntity<String> registerBookStore (
            @RequestBody RegisterBookStoreDTO bookStoreDTO) {
        boolean saved = bookStoreService.registerBookStore(bookStoreDTO);

        if (saved) return ResponseEntity.ok("Book store registered successfully");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book store could not be registered");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateBookStore (
            @PathVariable("userId") Integer userId,
            @RequestBody ProfileBookStoreDTO bookStoreDTO) {
        boolean updated = bookStoreService.updateBookStore(userId, bookStoreDTO);

        if (updated) return ResponseEntity.ok("BookStore updated successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("BookStore Not found!");
    }
}
