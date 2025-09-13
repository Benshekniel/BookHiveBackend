// 5. OrganizationFeedbackController.java
package controller.organization;

import model.dto.Organization.FeedbackDto.*;
import service.organization.impl.FeedbackServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/organization-feedback")
@RequiredArgsConstructor
public class OrganizationFeedbackController {

    private final FeedbackServiceImpl feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponseDto> createFeedback(@RequestBody FeedbackCreateDto feedbackData) {
        try {
            FeedbackResponseDto response = feedbackService.createFeedback(feedbackData);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<FeedbackResponseDto>> getFeedbackByOrganization(@PathVariable Long orgId) {
        List<FeedbackResponseDto> feedback = feedbackService.getFeedbackByOrganization(orgId);
        return ResponseEntity.ok(feedback);
    }
}