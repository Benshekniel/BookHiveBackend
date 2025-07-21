// controller/BookStoreController.java
package controller;

import lombok.RequiredArgsConstructor;
import model.dto.BookStore.BookStoreDTO.*;
import service.BookStore.BookStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookstores")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BookStoreController {

    private final BookStoreService bookStoreService;

    // Create bookstore
    @PostMapping
    public ResponseEntity<BookStoreResponseDTO> createBookStore(
            @RequestBody BookStoreCreateDTO createDTO,
            @RequestParam Integer userId) {  // Temporary - until authentication

        try {
            BookStoreResponseDTO createdBookStore = bookStoreService.createBookStore(createDTO, userId);
            return ResponseEntity.ok(createdBookStore);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get all bookstores (public)
    @GetMapping
    public ResponseEntity<List<BookStoreListDTO>> getAllBookStores() {
        List<BookStoreListDTO> bookStores = bookStoreService.getAllBookStores();
        return ResponseEntity.ok(bookStores);
    }

    // Get bookstore by ID (public view)
    @GetMapping("/{storeId}")
    public ResponseEntity<BookStorePublicDTO> getBookStoreById(@PathVariable Integer storeId) {
        Optional<BookStorePublicDTO> bookStore = bookStoreService.getBookStoreById(storeId);
        return bookStore.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get current user's bookstore (owner dashboard)
    @GetMapping("/my-store")
    public ResponseEntity<BookStoreResponseDTO> getMyBookStore(@RequestParam Integer userId) {
        Optional<BookStoreResponseDTO> bookStore = bookStoreService.getBookStoreByUserId(userId);
        return bookStore.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update bookstore
    @PutMapping("/{storeId}")
    public ResponseEntity<BookStoreResponseDTO> updateBookStore(
            @PathVariable Integer storeId,
            @RequestBody BookStoreUpdateDTO updateDTO,
            @RequestParam Integer userId) {

        updateDTO.setStoreId(storeId);

        try {
            BookStoreResponseDTO updatedBookStore = bookStoreService.updateBookStore(updateDTO, userId);
            return ResponseEntity.ok(updatedBookStore);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete bookstore
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteBookStore(
            @PathVariable Integer storeId,
            @RequestParam Integer userId) {

        try {
            bookStoreService.deleteBookStore(storeId, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Search bookstores
    @GetMapping("/search")
    public ResponseEntity<List<BookStoreListDTO>> searchBookStores(@RequestParam String query) {
        List<BookStoreListDTO> bookStores = bookStoreService.searchBookStores(query);
        return ResponseEntity.ok(bookStores);
    }

    // Get bookstores by type
    @GetMapping("/type/{booksType}")
    public ResponseEntity<List<BookStoreListDTO>> getBookStoresByType(@PathVariable String booksType) {
        try {
            List<BookStoreListDTO> bookStores = bookStoreService.getBookStoresByType(booksType);
            return ResponseEntity.ok(bookStores);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}