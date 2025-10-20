package service.Moderator.impl;

import jakarta.transaction.Transactional;
import model.dto.AllUsersDTO;
import model.entity.AllUsers;
import model.entity.BookStore;
import model.entity.Donation;
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

    @Override
    public List<Donation> getPendingDonations() {
        return moderatorRepo.getPendingDonations();
    }

    @Override
    public boolean approveDonation(Long donationId) {
        int updated = moderatorRepo.updateDonationStatus(donationId, "APPROVED");
        return updated > 0;
    }

    // Reject donation with reason
    @Transactional
    public String rejectDonation(Long donationId, String reason) {
        int updated = moderatorRepo.rejectDonation(donationId, reason);
        return updated > 0 ? "Donation rejected" : "Donation not found";
    }

    // Retrieve all approved donations
    @Override
    public List<Donation> getApprovedDonations() {
        return moderatorRepo.findAllApprovedDonations();
    }

    // Retrieve all rejected donations
    @Override
    public List<Donation> getRejectedDonations() {
        return moderatorRepo.findAllRejectedDonations();
    }

    @Override
    public List<BookStore> getPendingOrUnapprovedBookStores() {
        return moderatorRepo.findPendingOrUnapprovedBookStores();
    }

    @Override
    public List<BookStore> getRejectedBookStores() {
        return moderatorRepo.findRejectedBookStores();
    }

    @Override
    public List<BookStore> getApprovedBookStores() {
        return moderatorRepo.findApprovedBookStores();
    }

    @Override
    public String rejectBookStore(Integer userId) {
        int rowsAffected = moderatorRepo.rejectBookStoreByUserId(userId);
        moderatorRepo.rejectBookStoreById(userId);
        return rowsAffected > 0 ? "success" : "No BookStore found for user_id: " + userId;
    }

    @Override
    public String approveBookStore(Integer userId) {
        int rowsAffected = moderatorRepo.approveBookStoreByUserId(userId);
        moderatorRepo.activeBookStoreById(userId);
        return rowsAffected > 0 ? "success" : "No BookStore found for user_id: " + userId;
    }

    @Override
    public String banBookStore(Integer userId) {
        int rowsAffected = moderatorRepo.banBookStoreByUserId(userId);
        moderatorRepo.banBookStoreById(userId);
        return rowsAffected > 0 ? "success" : "No BookStore found for user_id: " + userId;
    }

}
