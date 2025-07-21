package controller;

import model.dto.ModeratorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
