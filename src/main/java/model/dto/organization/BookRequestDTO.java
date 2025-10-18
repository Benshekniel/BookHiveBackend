package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookRequestDTO {
    private Long id;
    private Long organizationId;
    private String title;
    private String subject;
    private Integer quantity;
    private String urgency;
    private String description;
    private String status;
    private String dateRequested;
    private String dateApproved;
    private String dateFulfilled;
    private String rejectionReason;
    private Long donorId;
}