// controller/UserContactController.java
package controller.Delivery;

import model.entity.AllUsers;
import service.Delivery.impl.UserContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserContactController {

    private final UserContactService userContactService;

    @GetMapping("/contacts/{userRole}/{userId}")
    public ResponseEntity<List<AllUsers>> getAvailableContactsByRole(
            @PathVariable String userRole,
            @PathVariable Long userId) {
        try {
            System.out.println("Getting contacts for role: " + userRole + ", userId: " + userId);
            List<AllUsers> contacts = userContactService.getAvailableContactsByRole(userRole, userId);
            System.out.println("Found " + contacts.size() + " contacts");
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            System.err.println("Error getting contacts: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<AllUsers>> getUsersByRole(@PathVariable String role) {
        try {
            List<AllUsers> users = userContactService.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.err.println("Error getting users by role: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}