package controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.Competition.CompetitionsParticipantEmailsService;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:9999") // same as your frontend port
@RequestMapping("/api")
public class competitionsParticipantController {

    @Autowired
    private CompetitionsParticipantEmailsService competitionsParticipantEmailsService;

    // ➕ Insert participant email
    @PostMapping("/insertParticipantEmail")
    public ResponseEntity<Map<String, String>> insertParticipantEmail(
            @RequestParam String competitionId,
            @RequestParam String email) {

        try {
            competitionsParticipantEmailsService.insertParticipant(competitionId, email);
            return ResponseEntity.ok(Map.of("message", "insert_success"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("message", "insert_failed", "error", e.getMessage()));
        }
    }

    // ✏️ Update participant email
    @PutMapping("/updateParticipantEmail")
    public ResponseEntity<Map<String, String>> updateParticipantEmail(
            @RequestParam String competitionId,
            @RequestParam String email) {

        try {
            competitionsParticipantEmailsService.updateParticipant(competitionId, email);
            return ResponseEntity.ok(Map.of("message", "update_success"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("message", "update_failed", "error", e.getMessage()));
        }
    }

    // 🗑️ Delete participant email
    @DeleteMapping("/deleteParticipantEmail")
    public ResponseEntity<Map<String, String>> deleteParticipantEmail(
            @RequestParam String competitionId,
            @RequestParam String email) {

        try {
            competitionsParticipantEmailsService.deleteParticipant(competitionId, email);
            return ResponseEntity.ok(Map.of("message", "delete_success"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("message", "delete_failed", "error", e.getMessage()));
        }
    }

}
