package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponseDTO {
    private Long feedbackId;
    private String subject;
    private String feedbackType;
    private String message;
    private Integer rating;
    private String category;
    private LocalDateTime submittedDate;
    private String status; // "submitted", "reviewed", "resolved"
    private String adminResponse;
    private LocalDateTime responseDate;
}
