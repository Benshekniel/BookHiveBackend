package controller;

import model.dto.AllUsersDTO;
import model.dto.CompetitionDTO;
import model.dto.UserBooksDTO;
import model.entity.Competitions;
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


    @GetMapping("/getPendingRegistrations")
    public ResponseEntity<List<Map<String, Object>>> getPendingRegistrations() {
        List<Map<String, Object>> pendings = moderatorService.getAllPending();
        return ResponseEntity.ok(pendings);
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
}
