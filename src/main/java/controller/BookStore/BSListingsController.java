package controller.BookStore;

import model.dto.BookStore.*;
import org.springframework.http.HttpStatus;
import service.BookStore.BSListingsService;
import service.BookStore.BookStoreService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/bs-listings")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BSListingsController {

    private final BookStoreService bookStoreService;
    private final BSListingsService listingsService;

    // sale details
    // lending details

    @GetMapping("/getBookListingSale/{userId}")
    public ResponseEntity<List<BSBookDTOs.BookListingDTO>> getBookListingSale (
            @PathVariable("userId") Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        List<BSBookDTOs.BookListingDTO> books = listingsService.getBookListingSale(storeId);
        if (books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/getBookListingLend/{userId}")
    public ResponseEntity<List<BSBookDTOs.BookListingDTO>> getBookListingLend (
            @PathVariable("userId") Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        List<BSBookDTOs.BookListingDTO> books = listingsService.getBookListingLend(storeId);
        if (books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook (
            @PathVariable("bookId") Integer bookId ) {

        boolean updated = listingsService.inventoryItem(bookId);
        if (updated) return ResponseEntity.ok("Book moved to inventory successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book Not found!");
    }

}
