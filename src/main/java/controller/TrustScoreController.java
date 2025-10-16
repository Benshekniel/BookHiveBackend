package controller;

import model.dto.TrustScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import service.TrustScore.TrustScoreService;


@RestController
@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
@RequestMapping("/api/trustscore")
public class TrustScoreController {

    @Autowired
    private TrustScoreService trustScoreService;

    @PostMapping("/initialize")
    public ResponseEntity<?> initializeTrustScore(@RequestBody TrustScoreDTO request) {
        try {
            trustScoreService.initial_Trustscore(request.getEmail());
            return ResponseEntity.ok(new TrustScoreDTO(request.getUserId(), 500, request.getEmail()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PutMapping("/adjust")
    public ResponseEntity<?> adjustTrustScore(@RequestBody TrustScoreDTO request) {
        try {
            trustScoreService.adjust_Trustscore(request.getEmail(), request.getScore());
            return ResponseEntity.ok(new TrustScoreDTO(request.getUserId(), request.getScore(), request.getEmail()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> fetchTrustScore(@PathVariable String email) {
        try {
            Integer score = trustScoreService.fetch_Trustscore(email);
            if (score == null) {
                return ResponseEntity.ok(new TrustScoreDTO(null, null, email));
            }
            return ResponseEntity.ok(new TrustScoreDTO(null, score, email));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/deduct")
    public ResponseEntity<?> deductTrustScore(@RequestBody TrustScoreDTO request) {
        try {
            trustScoreService.deduct_Trustscore(request.getEmail(), request.getScore());
            Integer updatedScore = trustScoreService.fetch_Trustscore(request.getEmail());
            return ResponseEntity.ok(new TrustScoreDTO(request.getUserId(), updatedScore, request.getEmail()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/add")
    public ResponseEntity<?> addTrustScore(@RequestBody TrustScoreDTO request) {
        try {
            trustScoreService.add_Trustscore(request.getEmail(), request.getScore());
            Integer updatedScore = trustScoreService.fetch_Trustscore(request.getEmail());
            return ResponseEntity.ok(new TrustScoreDTO(request.getUserId(), updatedScore, request.getEmail()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}