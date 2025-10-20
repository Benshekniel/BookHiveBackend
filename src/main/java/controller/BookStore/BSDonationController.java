package controller.BookStore;

import model.dto.BookStore.BSDonationDTO;
import model.dto.BookStore.BSInventoryDTOs;
import org.springframework.http.HttpStatus;
import service.BookStore.BSDonationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import service.BookStore.BSInventoryService;
import service.BookStore.BookStoreService;

import java.util.List;

@RestController
@RequestMapping("/api/bs-donation")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BSDonationController {

    private final BSDonationService donationService;
    private final BookStoreService bookStoreService;

    @GetMapping("/current-donations")
    public ResponseEntity<List<BSDonationDTO.DonationDetailsDTO>> getCurrentDonationEvents () {
        List<BSDonationDTO.DonationDetailsDTO> events = donationService.getCurrentDonationEvents();
        if (events.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/matching-list/{category}/{userId}")
    public ResponseEntity<List<BSInventoryDTOs.ConciseDonationDTO>> getInventoryListByCategory (
            @PathVariable String category,
            @PathVariable Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        List<BSInventoryDTOs.ConciseDonationDTO> itemList = donationService.getInventoryListByCategory(category, storeId);
        if (itemList.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(itemList);
    }

    @PutMapping("/contribute/{donationId}/{userId}")
    public ResponseEntity<String> addContributions (
            @PathVariable Long donationId,
            @PathVariable Integer userId,
            @RequestBody List<BSInventoryDTOs.ContributionDTO> contributions) {

        Integer storeId = bookStoreService.getStoreIdByUserId(userId);
        boolean success = donationService.fullDonationProcess(donationId, storeId, contributions);

        if (success) return ResponseEntity.ok("Successful contribution.");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong.");
    }

}
