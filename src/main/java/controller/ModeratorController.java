package controller;

import model.dto.AllUsersDTO;
import model.dto.CompetitionDTO;
import model.dto.UserBooksDTO;
import model.entity.BookStore;
import model.entity.Competitions;
import model.entity.Donation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.GoogleDriveUpload.FileStorageService;
import service.Moderator.CompetitionService;
import service.Moderator.ModeratorService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
@RequestMapping("/api/moderator")
public class ModeratorController {

//    @Autowired
//    private ModeratorService moderatorService;
//
//    @PostMapping("/add_Account")
//    public String moderatorAdd(@RequestBody ModeratorDto moderatorDto) {
//
//        String Id= moderatorService.addAccount(moderatorDto);
//        return Id;
//
//    }

    @Autowired
    private ModeratorService moderatorService;
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private FileStorageService fileStorageService;

    // Get count of active users
    @GetMapping("/countActiveUsers")
    public ResponseEntity<Map<String, Integer>> countActiveUsers() {
        int count = moderatorService.getActiveUserCount();
        return ResponseEntity.ok(Map.of("activeUsers", count));
    }

    // Get count of flagged (banned or disabled) users
    @GetMapping("/countFlaggedUsers")
    public ResponseEntity<Map<String, Integer>> countFlaggedUsers() {
        int count = moderatorService.getFlaggedUserCount();
        return ResponseEntity.ok(Map.of("flaggedUsers", count));
    }


    @GetMapping("/getPendingRegistrations")
    public ResponseEntity<List<Map<String, Object>>> getPendingRegistrations() {
        List<Map<String, Object>> pendings = moderatorService.getAllPending();
        return ResponseEntity.ok(pendings);
    }

    @GetMapping("/getFlaggedUsers")
    public ResponseEntity<List<Map<String, Object>>> getFlaggedUsers() {
        List<Map<String, Object>> flagged = moderatorService.getFlaggedUsers();
        return ResponseEntity.ok(flagged);
    }

    @GetMapping("/getActiveUsers")
    public ResponseEntity<List<Map<String, Object>>> getActiveUsers() {
        List<Map<String, Object>> flagged = moderatorService.getActiveUsers();
        return ResponseEntity.ok(flagged);
    }

    // Add a violation
    @PostMapping("/applyViolation")
    public ResponseEntity<String> applyViolation(
            @RequestParam("email") String email,
            @RequestParam("reason") String reason,
            @RequestParam("status") String status) {

        try {
            moderatorService.addViolation(email, reason, status);
            return ResponseEntity.ok("Violation added successfully for: " + email);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error adding violation: " + e.getMessage());
        }
    }

    // Get violation reason by email
    @GetMapping("/getViolationReason")
    public ResponseEntity<?> getViolationReason(@RequestParam("email") String email) {
        try {
            String reason = moderatorService.getViolationReason(email);

            if (reason != null && !reason.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "email", email,
                        "reason", reason
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                        "message", "No violation found for: " + email
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error", "Error fetching violation reason",
                            "details", e.getMessage()
                    ));
        }
    }



    // Remove a violation
    @DeleteMapping("/removeViolation")
    public ResponseEntity<String> removeViolation(@RequestParam("email") String email) {
        try {
            moderatorService.removeViolation(email);
            return ResponseEntity.ok("Violation removed successfully for: " + email);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error removing violation: " + e.getMessage());
        }
    }


    @GetMapping("/approveUser")
    public ResponseEntity<Map<String, String>> approveUser(
            @RequestParam("email") String email,
            @RequestParam("name") String name){
        String result = moderatorService.approveUserStatus(email,name);
        return ResponseEntity.ok(Map.of("message", result));
    }

    @GetMapping("/rejectUser")
    public ResponseEntity<Map<String, String>> rejectUser(
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("reason") String reason ){
        String result = moderatorService.rejectUserStatus(email,name,reason);
        return ResponseEntity.ok(Map.of("message", result));
    }

    @PostMapping("/createCompetition")
    public ResponseEntity<Map<String, String>> createCompetition (
            @RequestPart("competitionData") CompetitionDTO competitionDTO,
            @RequestParam("email") String email,
            @RequestPart("bannerImage") MultipartFile bannerImageFile )throws IOException {


        // Generate random filenames before user creation
        String bannerImageName = fileStorageService.generateRandomFilename(bannerImageFile);
        // Assign random filenames to DTO
        competitionDTO.setBannerImage(bannerImageName);


        String response = competitionService.createCompetition(competitionDTO,email,bannerImageName);
        if ("success".equals(response)) {
            Map<String, String> Result = fileStorageService.uploadFile(bannerImageFile, "competitions", bannerImageName);
        }
        return ResponseEntity.ok(Map.of("message", response));
    }

    @GetMapping("/getAllCompetitions")
    public ResponseEntity<List<Map<String, Object>>> getAllCompetitions() {
        List<Map<String, Object>> competitions = competitionService.getAllCompetitionsMapped();
        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/goLiveCompetition")
    public ResponseEntity<Map<String, String>> goLiveCompetition (
            @RequestParam("competitionId") String competitionId,
            @RequestParam("email") String email
           ){
        String result = competitionService.makeActive(competitionId,email);
        return ResponseEntity.ok(Map.of("message", result));
    }

    @GetMapping("/reLiveCompetition")
    public ResponseEntity<Map<String, String>> reLiveCompetition (
            @RequestParam("competitionId") String competitionId,
            @RequestParam("email") String email
    ){
        String result = competitionService.make_ReActive(competitionId,email);
        return ResponseEntity.ok(Map.of("message", result));
    }

    @GetMapping("/stopLiveCompetition")
    public ResponseEntity<Map<String, String>> stopLiveCompetition (
            @RequestParam("competitionId") String competitionId,
            @RequestParam("email") String email
    ){
        String result = competitionService.stopActive(competitionId,email);
        return ResponseEntity.ok(Map.of("message", result));
    }

    @GetMapping("/pauseCompetition")
    public ResponseEntity<Map<String, String>> pauseCompetition (
            @RequestParam("competitionId") String competitionId,
            @RequestParam("email") String email
    ){
        String result = competitionService.makePause(competitionId,email);
        return ResponseEntity.ok(Map.of("message", result));
    }

    @GetMapping("/resumeCompetition")
    public ResponseEntity<Map<String, String>> resumeCompetition (
            @RequestParam("competitionId") String competitionId,
            @RequestParam("email") String email
    ){
        String result = competitionService.makeResume(competitionId,email);
        return ResponseEntity.ok(Map.of("message", result));
    }

    @GetMapping("/getPendingDonations")
    public ResponseEntity<List<Donation>> getPendingDonations() {
        List<Donation> pendingDonations = moderatorService.getPendingDonations();
        return ResponseEntity.ok(pendingDonations);
    }

    // 🔹 Endpoint to approve a donation by ID
    @PutMapping("/approveDonation/{id}")
    public ResponseEntity<String> approveDonation(@PathVariable("id") Long id) {
        boolean success = moderatorService.approveDonation(id);

        if (success) {
            return ResponseEntity.ok("Donation with ID " + id + " has been approved.");
        } else {
            return ResponseEntity.badRequest().body("No donation found with ID: " + id);
        }
    }

    // ✅ Reject a donation with reason
    @PutMapping("/rejectDonation/{id}")
    public ResponseEntity<String> rejectDonation(@PathVariable Long id, @RequestParam String reason) {
        return ResponseEntity.ok(moderatorService.rejectDonation(id, reason));
    }

    // ✅ Get all approved donations
    @GetMapping("/getApprovedDonations")
    public List<Donation> getApprovedDonations() {
        return moderatorService.getApprovedDonations();
    }

    // ✅ Get all rejected donations
    @GetMapping("/getRejectedDonations")
    public List<Donation> getRejectedDonations() {
        return moderatorService.getRejectedDonations();
    }

    @GetMapping("/bookstores/pending")
    public ResponseEntity<List<BookStore>> getPendingOrUnapprovedBookStores() {
        List<BookStore> bookStores = moderatorService.getPendingOrUnapprovedBookStores();
        return ResponseEntity.ok(bookStores);
    }

    @GetMapping("/bookstores/approved")
    public ResponseEntity<List<BookStore>> getApprovedBookStores() {
        List<BookStore> bookStores = moderatorService.getApprovedBookStores();
        return ResponseEntity.ok(bookStores);
    }

    @GetMapping("/bookstores/rejected")
    public ResponseEntity<List<BookStore>> getRejectedBookStores() {
        List<BookStore> bookStores = moderatorService.getRejectedBookStores();
        return ResponseEntity.ok(bookStores);
    }

    @PostMapping("/bookstores/setReject")
    public ResponseEntity<Map<String, String>> rejectBookStore(@RequestParam("userId") Integer userId) {
        String response = moderatorService.rejectBookStore(userId);
        return ResponseEntity.ok(Map.of("message", response));
    }

    @PostMapping("/bookstores/setApprove")
    public ResponseEntity<Map<String, String>> approveBookStore(@RequestParam("userId") Integer userId) {
        String response = moderatorService.approveBookStore(userId);
        return ResponseEntity.ok(Map.of("message", response));
    }

}
