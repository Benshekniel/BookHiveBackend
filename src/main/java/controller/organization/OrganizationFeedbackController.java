//package controller.organization;
//
//import model.dto.organization.FeedbackDTO;
//import model.dto.organization.FeedbackCreateDTO;
//import service.organization.FeedbackService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.validation.Valid;
//
//import java.util.List;
//@CrossOrigin(origins = {"http://localhost:9999", "http://localhost:3000"})
//
//@RestController
//@RequestMapping("/api/organization-feedback")
//public class OrganizationFeedbackController {
//
//    private final FeedbackService feedbackService;
//
//    @Autowired
//    public OrganizationFeedbackController(FeedbackService feedbackService) {
//        this.feedbackService = feedbackService;
//    }
//
//    @PostMapping
//    public ResponseEntity<FeedbackDTO> createFeedback(@Valid @RequestBody FeedbackCreateDTO createDTO) {
//        FeedbackDTO created = feedbackService.createFeedback(createDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }
//
//    @GetMapping("/organization/{organizationId}")
//    public ResponseEntity<List<FeedbackDTO>> getFeedbackByOrganization(@PathVariable Long organizationId) {
//        List<FeedbackDTO> feedback = feedbackService.getFeedbackByOrganization(organizationId);
//        return ResponseEntity.ok(feedback);
//    }
//
//    @GetMapping("/donation/{donationId}")
//    public ResponseEntity<FeedbackDTO> getFeedbackByDonation(@PathVariable Long donationId) {
//        FeedbackDTO feedback = feedbackService.getFeedbackByDonation(donationId);
//        return ResponseEntity.ok(feedback);
//    }
//}