package controller.Admin;

import model.dto.ModeratorDto;
import model.entity.Moderator;
import service.Admin.AdminModeratorService;
import util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:9999")
@RequestMapping("/api/admin")
public class AdminModeratorController {

    @Autowired
    private AdminModeratorService moderatorService;

    // ==================== CREATE OPERATIONS ====================

    // ==================== READ OPERATIONS ====================

    // Get moderator by ID
    @GetMapping("/moderators/{id}")
    public ResponseEntity<MessageResponse> getModeratorById(@PathVariable Long id) {
        try {
            ModeratorDto moderatorDto = moderatorService.getModeratorDtoById(id);
            return ResponseEntity.ok(MessageResponse.success("Moderator found", moderatorDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageResponse.error("Moderator not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    // Get all moderators
    @GetMapping("/moderators")
    public ResponseEntity<MessageResponse> getAllModerators() {
        try {
            List<ModeratorDto> moderators = moderatorService.getAllModeratorsDto();
            return ResponseEntity.ok(MessageResponse.success("Moderators retrieved successfully", moderators));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to retrieve moderators: " + e.getMessage()));
        }
    }


    // Get moderator by email
    @GetMapping("/moderators/email/{email}")
    public ResponseEntity<MessageResponse> getModeratorByEmail(@PathVariable String email) {
        try {
            ModeratorDto moderatorDto = moderatorService.findByEmailDto(email);
            return ResponseEntity.ok(MessageResponse.success("Moderator found", moderatorDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageResponse.error("Moderator not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    // Search moderators by name or email
    //@GetMapping("/moderators/search")
    public ResponseEntity<MessageResponse> searchModerators(
            @RequestParam(required = false) String searchTerm) {
        try {
            List<ModeratorDto> moderators = moderatorService.searchModeratorsDto(searchTerm);
            return ResponseEntity.ok(MessageResponse.success("Search completed successfully", moderators));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Search failed: " + e.getMessage()));
        }
    }

    // Get moderators by city
    @GetMapping("/moderators/city/{city}")
    public ResponseEntity<MessageResponse> getModeratorsByCity(@PathVariable String city) {
        try {
            List<ModeratorDto> moderators = moderatorService.getModeratorsByCityDto(city);
            return ResponseEntity.ok(MessageResponse.success("Moderators retrieved successfully", moderators));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to retrieve moderators: " + e.getMessage()));
        }
    }

    // Get moderators by minimum experience
    @GetMapping("/moderators/experience/{minExperience}")
    public ResponseEntity<MessageResponse> getModeratorsByExperience(@PathVariable Integer minExperience) {
        try {
            List<ModeratorDto> moderators = moderatorService.getModeratorsByExperienceDto(minExperience);
            return ResponseEntity.ok(MessageResponse.success("Moderators retrieved successfully", moderators));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to retrieve moderators: " + e.getMessage()));
        }
    }

    // Get moderators by age range
    @GetMapping("/moderators/age")
    public ResponseEntity<MessageResponse> getModeratorsByAgeRange(
            @RequestParam int minAge,
            @RequestParam int maxAge) {
        try {
            List<Moderator> moderators = moderatorService.getModeratorsByAgeRange(minAge, maxAge);
            List<ModeratorDto> moderatorDtos = moderatorService.convertToDtoList(moderators);
            return ResponseEntity.ok(MessageResponse.success("Moderators retrieved successfully", moderatorDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to retrieve moderators: " + e.getMessage()));
        }
    }

    // Get moderators count
    @GetMapping("/moderators/count")
    public ResponseEntity<MessageResponse> getModeratorCount() {
        try {
            long count = moderatorService.getModeratorCount();
            return ResponseEntity.ok(MessageResponse.success("Moderator count retrieved successfully", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to get moderator count: " + e.getMessage()));
        }
    }

    // Get moderator's age
    @GetMapping("/moderators/{id}/age")
    public ResponseEntity<MessageResponse> getModeratorAge(@PathVariable Long id) {
        try {
            int age = moderatorService.getModeratorAge(id);
            return ResponseEntity.ok(MessageResponse.success("Moderator age retrieved successfully", age));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageResponse.error("Moderator not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to get moderator age: " + e.getMessage()));
        }
    }

    // Check if email exists
    @GetMapping("/moderators/check-email")
    public ResponseEntity<MessageResponse> checkEmailExists(@RequestParam String email) {
        try {
            boolean exists = moderatorService.existsByEmail(email);
            return ResponseEntity.ok(MessageResponse.success("Email check completed", exists));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to check email: " + e.getMessage()));
        }
    }

    // Check if moderator exists by ID
    @GetMapping("/moderators/exists/{id}")
    public ResponseEntity<MessageResponse> checkModeratorExists(@PathVariable Long id) {
        try {
            boolean exists = moderatorService.existsById(id);
            return ResponseEntity.ok(MessageResponse.success("Moderator existence check completed", exists));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to check moderator existence: " + e.getMessage()));
        }
    }

    // ==================== UPDATE OPERATIONS ====================

    // Update moderator details
    @PutMapping("/moderators/{id}")
    public ResponseEntity<MessageResponse> updateModerator(
            @PathVariable Long id,
            @RequestBody ModeratorDto moderatorDto) {
        try {
            Moderator moderator = moderatorService.updateModerator(id, moderatorDto);
            ModeratorDto responseDto = moderatorService.convertToDto(moderator);
            return ResponseEntity.ok(MessageResponse.success("Moderator updated successfully", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to update moderator: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    // ==================== DELETE OPERATIONS ====================

    // Delete moderator by ID
    @DeleteMapping("/moderators/{id}")
    public ResponseEntity<MessageResponse> deleteModerator(@PathVariable Long id) {
        try {
            // Check if moderator exists before deletion
            if (!moderatorService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(MessageResponse.error("Moderator not found with id: " + id));
            }

            moderatorService.deleteModerator(id);
            return ResponseEntity.ok(MessageResponse.success("Moderator deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to delete moderator: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    // Delete moderator by email
    @DeleteMapping("/moderators/email/{email}")
    public ResponseEntity<MessageResponse> deleteModeratorByEmail(@PathVariable String email) {
        try {
            moderatorService.deleteModeratorByEmail(email);
            return ResponseEntity.ok(MessageResponse.success("Moderator deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageResponse.error("Failed to delete moderator: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    // Delete multiple moderators by IDs
    @DeleteMapping("/moderators/batch")
    public ResponseEntity<MessageResponse> deleteModerators(@RequestBody List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(MessageResponse.error("No moderator IDs provided"));
            }

            moderatorService.deleteModerators(ids);
            return ResponseEntity.ok(MessageResponse.success("Moderators deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to delete moderators: " + e.getMessage()));
        }
    }

    // Delete all moderators by city
    @DeleteMapping("/moderators/city/{city}")
    public ResponseEntity<MessageResponse> deleteModeratorsByCity(@PathVariable String city) {
        try {
            int deletedCount = moderatorService.deleteModeratorsByCity(city);
            return ResponseEntity.ok(MessageResponse.success(
                    "Successfully deleted " + deletedCount + " moderators from " + city, deletedCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to delete moderators: " + e.getMessage()));
        }
    }

    // Soft delete moderator (placeholder for future implementation)
    @PatchMapping("/moderators/{id}/soft-delete")
    public ResponseEntity<MessageResponse> softDeleteModerator(@PathVariable Long id) {
        try {
            moderatorService.softDeleteModerator(id);
            return ResponseEntity.ok(MessageResponse.success("Moderator soft deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageResponse.error("Failed to soft delete moderator: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Internal server error: " + e.getMessage()));
        }
    }
}