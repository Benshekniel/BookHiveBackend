package controller;

import model.entity.TrustScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.Moderator.TrustScoreRegulationService;
import service.TrustScore.TrustScoreService;

@RestController
@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
@RequestMapping("/api/regulation")
public class TrustScoreRegulationController {


    @Autowired
    private TrustScoreRegulationService regulationService;

    // ✅ Retrieve trust score by email
    @GetMapping("/get/{email}")
    public ResponseEntity<TrustScore> getTrustScore(@PathVariable String email) {
        return ResponseEntity.ok(regulationService.getTrustScoreByEmail(email));
    }

    // ✅ Increase Review
    @PutMapping("/increase/review/{email}")
    public ResponseEntity<String> increaseReview(@PathVariable String email) {
        regulationService.increaseReview(email);
        return ResponseEntity.ok("Trust score increased for REVIEW");
    }

    // ✅ Increase Purchase
    @PutMapping("/increase/purchase/{email}")
    public ResponseEntity<String> increasePurchase(@PathVariable String email) {
        regulationService.increasePurchase(email);
        return ResponseEntity.ok("Trust score increased for PURCHASE");
    }

    // ✅ Increase Competition Join
    @PutMapping("/increase/compjoin/{email}")
    public ResponseEntity<String> increaseCompJoin(@PathVariable String email) {
        regulationService.increaseCompJoin(email);
        return ResponseEntity.ok("Trust score increased for COMPJOIN");
    }

    // ✅ Decrease Negative
    @PutMapping("/decrease/negative/{email}")
    public ResponseEntity<String> decreaseNegative(@PathVariable String email) {
        regulationService.decreaseNegative(email);
        return ResponseEntity.ok("Trust score decreased for NEGATIVE");
    }

    // ✅ Increase Positive
    @PutMapping("/increase/positive/{email}")
    public ResponseEntity<String> increasePositive(@PathVariable String email) {
        regulationService.increasePositive(email);
        return ResponseEntity.ok("Trust score increased for POSITIVE");
    }

    // ✅ Update each rule amount
    @PutMapping("/update/review/{amount}")
    public ResponseEntity<String> updateReview(@PathVariable Integer amount) {
        regulationService.updateReviewAmount(amount);
        return ResponseEntity.ok("Updated REVIEW amount to " + amount);
    }

    @PutMapping("/update/purchase/{amount}")
    public ResponseEntity<String> updatePurchase(@PathVariable Integer amount) {
        regulationService.updatePurchaseAmount(amount);
        return ResponseEntity.ok("Updated PURCHASE amount to " + amount);
    }

    @PutMapping("/update/compjoin/{amount}")
    public ResponseEntity<String> updateCompJoin(@PathVariable Integer amount) {
        regulationService.updateCompJoinAmount(amount);
        return ResponseEntity.ok("Updated COMPJOIN amount to " + amount);
    }

    @PutMapping("/update/negative/{amount}")
    public ResponseEntity<String> updateNegative(@PathVariable Integer amount) {
        regulationService.updateNegativeAmount(amount);
        return ResponseEntity.ok("Updated NEGATIVE amount to " + amount);
    }

    @PutMapping("/update/positive/{amount}")
    public ResponseEntity<String> updatePositive(@PathVariable Integer amount) {
        regulationService.updatePositiveAmount(amount);
        return ResponseEntity.ok("Updated POSITIVE amount to " + amount);
    }

    // ✅ Get amount for a specific rule
    @GetMapping("/amount/{rule}")
    public ResponseEntity<Integer> getAmountForRule(@PathVariable String rule) {
        Integer amount = regulationService.getAmountForRule(rule);
        if (amount != null) {
            return ResponseEntity.ok(amount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
