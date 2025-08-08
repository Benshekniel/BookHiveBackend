package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackCreateDTO {
    private String subject;
    private String feedbackType; // "complaint", "suggestion", "compliment", "general"
    private String message;
    private Integer rating; // 1-5 stars
    private String category; // "service", "platform", "donation", "request"
}
