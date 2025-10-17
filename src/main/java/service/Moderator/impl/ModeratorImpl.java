package service.Moderator.impl;

import model.dto.AllUsersDTO;
import model.entity.AllUsers;
import model.repo.AllUsersRepo;
import model.repo.ModeratorRepo;
import model.repo.ViolationsUserRepo;
import org.springframework.http.ResponseEntity;
import service.Email.EmailService;
import service.Moderator.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModeratorImpl implements ModeratorService {

    @Autowired
    private ModeratorRepo moderatorRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    @Autowired
    private ViolationsUserRepo violationsUserRepository;


    public List<Map<String, Object>> getAllPending() {
        return moderatorRepo.findAllPending();
    }

    public List<Map<String, Object>> getFlaggedUsers() {
        return moderatorRepo.findAllFlagged();
    }

    public List<Map<String, Object>> getActiveUsers() {
        return moderatorRepo.findAllActive();
    }


    @Override
    public String approveUserStatus(String email,String name) {

        int updatedRows = moderatorRepo.approveUserByEmail(email);
        String response = updatedRows > 0 ? "Approved" : "No user found to approve";

        if("Approved".equals(response)){
            String to=email;
            String subject="You account status has been changed";
            String text = "Congratulations! Your account has been approved and is now active.";
            String recipientName = name;
            try {
                emailService.sendEmailCustom(to, subject, text, recipientName, null,"email","approve.png");
            } catch (IOException e) {
                response= "Error sending email: ";
            }
        }

        return response;
    }

    @Override
    public String rejectUserStatus(String email,String name,String reason) {

        int updatedRows = moderatorRepo.rejectUserByEmail(email);
        String response = updatedRows > 0 ? "Rejected" : "No user found to approve";

        if("Rejected".equals(response)){
            String to=email;
            String subject="You account status has been changed";
            String text = reason;
            String recipientName = name;
            try {
                emailService.sendEmailCustom(to, subject, text, recipientName, null,"email","reject.png");
            } catch (IOException e) {
                response= "Error sending email: ";
            }
        }

        return response;
    }

    // Add a violation
    public void addViolation(String email, String reason, String status) {
        // Step 1: Add the violation record
        violationsUserRepository.insertViolation(email, reason);

        // Step 2: Change user status based on parameter
        if ("banned".equalsIgnoreCase(status)) {
            moderatorRepo.banUserByEmail(email);
        } else if ("disabled".equalsIgnoreCase(status)) {
            moderatorRepo.disableUserByEmail(email);
        } else {
            System.out.println("⚠ Unknown status type: " + status);
        }
    }


    // Delete a violation by email
    @Override
    public void removeViolation(String email) {
        violationsUserRepository.deleteByEmail(email);
        moderatorRepo.activeUserByEmail(email);
    }

    @Override
    public String getViolationReason(String email) {
        return violationsUserRepository.findReasonByEmail(email);
    }

    @Override
    public int getActiveUserCount() {
        return moderatorRepo.countActiveUsers();
    }

    @Override
    public int getFlaggedUserCount() {
        return moderatorRepo.countFlaggedUsers();
    }


}
