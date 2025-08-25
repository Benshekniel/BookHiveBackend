package controller.BookStore;

import model.dto.BookStore.*;
import service.BookStore.BSBookService;
import service.BookStore.BSInventoryService;
import service.BookStore.BookStoreService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/bs-inventory")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BSInventoryController {

    private final BookStoreService bookStoreService;
    private final BSBookService bookService;
    private final BSInventoryService inventoryService;

    // sale details
    // lending details

    @GetMapping("/getBookListingSale/{userId}")
    public ResponseEntity<List<BSBookDTOs.BookListingDTO>> getBookListingSale (
            @PathVariable("userId") Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        List<BSBookDTOs.BookListingDTO> books = inventoryService.getBookListingSale(storeId);
        if (books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/getBookListingLending/{userId}")
    public ResponseEntity<List<BSBookDTOs.BookListingDTO>> getBookListingLending (
            @PathVariable("userId") Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        List<BSBookDTOs.BookListingDTO> books = inventoryService.getBookListingLending(storeId);
        if (books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }

}
