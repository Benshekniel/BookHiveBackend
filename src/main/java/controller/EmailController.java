package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.Email.EmailService;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("text") String text,
            @RequestParam(value = "recipientName", required = false) String recipientName,
            @RequestPart(value = "attachment", required = false) MultipartFile attachment) {
        try {
            emailService.sendEmail(to, subject, text, recipientName, attachment);
            return ResponseEntity.ok("Email sent successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        }
    }
}
