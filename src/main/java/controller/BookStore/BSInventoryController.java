package controller.BookStore;

import model.dto.BookStore.*;
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
    private final BSInventoryService inventoryService;

    // regular book inventory details
    // donation book inventory details

    @GetMapping("/getRegularInventory/{userId}")
    public ResponseEntity<List<BSBookDTOs.BookListingDTO>> getRegularInventory (
            @PathVariable("userId") Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        List<BSBookDTOs.BookListingDTO> books = inventoryService.getRegularInventory(storeId);
        if (books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/getDonationInventory/{userId}")
    public ResponseEntity<List<BSBookDTOs.BookListingDTO>> getDonationInventory (
            @PathVariable("userId") Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        List<BSBookDTOs.BookListingDTO> books = inventoryService.getDonationInventory(storeId);
        if (books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }
}
