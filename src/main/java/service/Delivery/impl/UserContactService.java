package service.Delivery.impl;

import model.entity.AllUsers;
import model.repo.AllUsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserContactService {

    private final AllUsersRepo allUsersRepo;

    public List<AllUsers> getAvailableContactsByRole(String userRole, Long userId) {
        try {
            List<String> allowedRoles = getRoleBasedContacts(userRole.toLowerCase());
            System.out.println("Allowed roles for " + userRole + ": " + allowedRoles);

            // Use the custom query method from your repo
            List<AllUsers> allContacts = allUsersRepo.findContactsByRolesExcludingUser(
                    allowedRoles,
                    userId.intValue()
            );

            // FIXED: Filter out current user if somehow included
            List<AllUsers> filteredContacts = allContacts.stream()
                    .filter(contact -> contact.getUser_id() != userId.intValue()) // FIXED: use == for int comparison
                    .filter(contact -> contact.getStatus() == null || contact.getStatus() == AllUsers.Status.active) // FIXED: use lowercase active and handle null
                    .collect(Collectors.toList());

            System.out.println("Found " + filteredContacts.size() + " contacts for user " + userId);

            // Log contact details for debugging
            filteredContacts.forEach(contact ->
                    System.out.println("Contact: ID=" + contact.getUser_id() +
                            ", Name=" + contact.getName() +
                            ", Role=" + contact.getRole())
            );

            return filteredContacts;
        } catch (Exception e) {
            System.err.println("Error in getAvailableContactsByRole: " + e.getMessage());
            e.printStackTrace();

            // Fallback: get all active users except current user and filter by role
            try {
                List<AllUsers> allActiveUsers = allUsersRepo.findAllActiveUsersExcluding(userId.intValue());
                List<String> allowedRoles = getRoleBasedContacts(userRole.toLowerCase());

                return allActiveUsers.stream()
                        .filter(user -> user.getUser_id() != userId.intValue()) // FIXED: use == for int comparison
                        .filter(user -> allowedRoles.contains(user.getRole().toLowerCase()))
                        .filter(user -> user.getStatus() == null || user.getStatus() == AllUsers.Status.active) // FIXED: use lowercase active and handle null
                        .collect(Collectors.toList());
            } catch (Exception fallbackError) {
                System.err.println("Fallback also failed: " + fallbackError.getMessage());
                return Arrays.asList(); // Return empty list if all fails
            }
        }
    }

    public List<AllUsers> getUsersByRole(String role) {
        try {
            return allUsersRepo.findByRole(role);
        } catch (Exception e) {
            System.err.println("Error getting users by role: " + e.getMessage());
            return Arrays.asList();
        }
    }

    private List<String> getRoleBasedContacts(String userRole) {
        switch (userRole) {
            case "admin":
                return Arrays.asList("moderator", "manager", "organization", "hubmanager");
            case "moderator":
                return Arrays.asList("admin", "user", "agent", "bookstore", "hubmanager");
            case "bookstore":
                return Arrays.asList("user", "moderator", "manager");
            case "manager":
                return Arrays.asList("agent", "hubmanager", "admin", "moderator");
            case "agent":
                return Arrays.asList("manager", "hubmanager");
            case "hubmanager":
                return Arrays.asList("agent", "manager", "admin", "moderator");
            case "organization":
                return Arrays.asList("admin", "moderator");
            case "user":
                return Arrays.asList("moderator", "bookstore");
            default:
                return Arrays.asList();
        }
    }

    // Additional helper methods
    public List<AllUsers> getAllActiveUsers(int excludeUserId) {
        List<AllUsers> users = allUsersRepo.findAllActiveUsersExcluding(excludeUserId);
        // Extra safety filter
        return users.stream()
                .filter(user -> user.getUser_id() != excludeUserId) // FIXED: use == for int comparison
                .collect(Collectors.toList());
    }

    public List<AllUsers> getUsersByRoleExcludingUser(String role, int excludeUserId) {
        List<AllUsers> users = allUsersRepo.findByRoleExcludingUser(role, excludeUserId);
        // Extra safety filter
        return users.stream()
                .filter(user -> user.getUser_id() != excludeUserId) // FIXED: use == for int comparison
                .collect(Collectors.toList());
    }

    // FIXED: Validation method to prevent self-messaging
    public boolean canUserMessageContact(Long senderId, Long receiverId, String senderRole) {
        // Prevent self-messaging
        if (senderId.equals(receiverId)) {
            System.out.println("BLOCKED: User trying to message themselves");
            return false;
        }

        // Check if receiver is in allowed contacts for sender role
        List<String> allowedRoles = getRoleBasedContacts(senderRole.toLowerCase());

        try {
            AllUsers receiver = allUsersRepo.findById(receiverId.intValue()).orElse(null);
            if (receiver == null) {
                System.out.println("BLOCKED: Receiver not found");
                return false;
            }

            // Check if receiver has active status (or null which we treat as active)
            if (receiver.getStatus() != null && receiver.getStatus() != AllUsers.Status.active) {
                System.out.println("BLOCKED: Receiver is not active");
                return false;
            }

            boolean isAllowed = allowedRoles.contains(receiver.getRole().toLowerCase());
            System.out.println("Message permission check - Sender: " + senderId +
                    " (" + senderRole + "), Receiver: " + receiverId +
                    " (" + receiver.getRole() + "), Allowed: " + isAllowed);

            return isAllowed;
        } catch (Exception e) {
            System.err.println("Error checking message permission: " + e.getMessage());
            return false;
        }
    }
}