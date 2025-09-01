package controller.organization;

import model.dto.organization.FeedbackCreateDTO;
import model.dto.organization.FeedbackResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.organization.FeedbackService;

import java.util.List;

@RestController
@RequestMapping("/api/organization-feedback")
public class OrganizationFeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public FeedbackResponseDTO createFeedback(@RequestBody FeedbackCreateDTO dto) {
        return feedbackService.createFeedback(dto);
    }

    @GetMapping("/organization/{orgId}")
    public List<FeedbackResponseDTO> getFeedbacksByOrganization(@PathVariable Long orgId) {
        return feedbackService.getFeedbacksByOrganization(orgId);
    }
}
