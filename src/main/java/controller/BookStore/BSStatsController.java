package controller.BookStore;

import model.dto.BookStore.BSStatDTOs;
import org.springframework.http.ResponseEntity;
import service.BookStore.BSStatsService;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import service.BookStore.BookStoreService;

@RestController
@RequestMapping("/api/bs-stats")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BSStatsController {

    private final BSStatsService statsService;
    private final BookStoreService bookStoreService;

    @GetMapping("/regularInventory/{userId}")
    public ResponseEntity<BSStatDTOs.RegularInventoryStatDTO> getRegularInventoryStats(
            @PathVariable Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        BSStatDTOs.RegularInventoryStatDTO stats = statsService.getRegularInventoryStats(storeId);
        if (stats == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/donationInventory/{userId}")
    public ResponseEntity<BSStatDTOs.DonationInventoryStatDTO> getDonationInventoryStats(
            @PathVariable Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        BSStatDTOs.DonationInventoryStatDTO stats = statsService.getDonationInventoryStats(storeId);
        if (stats == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/saleListings/{userId}")
    public ResponseEntity<BSStatDTOs.SaleStatDTO> getSaleListingsStats(
            @PathVariable Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        BSStatDTOs.SaleStatDTO stats = statsService.getSaleListingsStats(storeId);
        if (stats == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/lendListings/{userId}")
    public ResponseEntity<BSStatDTOs.LendStatDTO> getLendListingsStats(
            @PathVariable Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        BSStatDTOs.LendStatDTO stats = statsService.getLendListingsStats(storeId);
        if (stats == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(stats);
    }
}
