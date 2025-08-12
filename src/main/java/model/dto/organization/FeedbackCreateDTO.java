package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackCreateDTO {
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getFeedbackType() { return feedbackType; }
    public void setFeedbackType(String feedbackType) { this.feedbackType = feedbackType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    private String subject;
    private String feedbackType; // "complaint", "suggestion", "compliment", "general"
    private String message;
    private Integer rating; // 1-5 stars
    private String category; // "service", "platform", "donation", "request"
}
