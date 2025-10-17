package service.Moderator;

import model.dto.AllUsersDTO;
import model.dto.UserBooksDTO;

import java.util.List;
import java.util.Map;

public interface ModeratorService {

    List<Map<String, Object>> getAllPending();

    List<Map<String, Object>> getActiveUsers();

    List<Map<String, Object>> getFlaggedUsers();

    String approveUserStatus(String email,String name);

    String rejectUserStatus(String email,String reason,String name);

    // Add a violation
    public void addViolation(String email, String reason,String status);
    // Delete a violation by email
    public void removeViolation(String email);
    // Fetch the violation by email
    public String getViolationReason(String email);

    public int getActiveUserCount();

    public int getFlaggedUserCount();


}

