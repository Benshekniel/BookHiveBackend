package controller;

import model.dto.BookStoreDTOs.RegisterBookStoreDTO;
import model.dto.BookStoreDTOs.ProfileBookStoreDTO;
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
        return bookStoreService.registerBookStore(bookStoreDTO);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<ProfileBookStoreDTO> getBookStore (
            @PathVariable("storeId") Integer storeId) {
        return bookStoreService.getBookStoreById(storeId);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<String> updateBookStore (
            @PathVariable("storeId") Integer storeId,
            @RequestBody ProfileBookStoreDTO bookStoreDTO) {

        return bookStoreService.updateBookStore(storeId, bookStoreDTO);
    }
}
