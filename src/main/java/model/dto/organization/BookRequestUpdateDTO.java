package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestUpdateDTO {
    private String title;
    private String subject;
    private Integer quantity;
    private String description;
    private String urgency;
    private String category;
    private String gradeLevel;
    private String notes;
    private String status;
}
