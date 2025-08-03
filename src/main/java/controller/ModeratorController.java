package controller;

import model.dto.AllUsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.Moderator.ModeratorService;

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

    @GetMapping("/getPendingRegistrations")
    public ResponseEntity<List<Map<String, Object>>> getPendingRegistrations() {
        List<Map<String, Object>> pendings = moderatorService.getAllPending();
        return ResponseEntity.ok(pendings);
    }
}
