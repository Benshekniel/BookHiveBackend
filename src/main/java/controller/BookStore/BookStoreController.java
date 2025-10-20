package controller.BookStore;

import model.dto.BookStore.BSStatDTOs;
import model.dto.BookStore.BookStoreDTOs;
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

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateBookStore (
            @PathVariable("userId") Integer userId,
            @RequestBody ProfileBookStoreDTO bookStoreDTO) {
        boolean updated = bookStoreService.updateBookStore(userId, bookStoreDTO);

        if (updated) return ResponseEntity.ok("BookStore updated successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("BookStore Not found!");
    }

    @PutMapping("/password-change")
    public ResponseEntity<String> changePassword (
            @RequestBody BookStoreDTOs.PassChangeDTO passChangeDTO ) {
        boolean changed = bookStoreService.changePassword(passChangeDTO);

        if (changed) return ResponseEntity.ok("BookStore password changed successfully");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BookStore password could not be changed!");
    }

    @GetMapping("/store-details/{userId}")
    public ResponseEntity<ProfileBookStoreDTO> getStoreProfileDetails (
            @PathVariable("userId") Integer userId ) {

        ProfileBookStoreDTO store = bookStoreService.getStoreProfileDetails(userId);
        if (store == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(store);
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<BSStatDTOs.DashboardStatsDTO> getBookStoreStats (
            @PathVariable("userId") Integer userId ) {

        BSStatDTOs.DashboardStatsDTO stats = bookStoreService.getBookStoreStats(userId);
        if (stats == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(stats);
    }
}
