package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeedbackDTO {
    private Long id;
    private Long organizationId;
    private Long donationId;
    private String donorName;
    private String bookTitle;
    private Integer rating;
    private String comment;
    private String date;
}