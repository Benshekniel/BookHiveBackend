package controller.BookStore;

import model.dto.BookStore.BSDonationDTO;
import model.dto.BookStore.BSInventoryDTOs;
import service.BookStore.BSDonationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/bs-donation")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BSDonationController {

    private final BSDonationService donationService;

    @GetMapping("/current-donations")
    public ResponseEntity<List<BSDonationDTO.DonationDetailsDTO>> getCurrentDonationEvents () {
        List<BSDonationDTO.DonationDetailsDTO> events = donationService.getCurrentDonationEvents();
        if (events.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/matching-list/{category}")
    public ResponseEntity<?> getInventoryListByCategory (
            @PathVariable String category) {
        List<BSInventoryDTOs.ConciseDonationDTO> itemList = donationService.getInventoryListByCategory(category);
        if (itemList.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(itemList);
    }

}
