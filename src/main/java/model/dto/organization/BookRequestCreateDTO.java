package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestCreateDTO {
    private String title;
    private String subject;
    private Integer quantity;
    private String description;
    private String urgency; // "high", "medium", "low"
    private String category; // "textbook", "reference", "literature", etc.
    private String gradeLevel;
    private String notes;
}
