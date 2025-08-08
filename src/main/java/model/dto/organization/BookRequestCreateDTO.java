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
    
    // Explicit getters
    public String getTitle() { return title; }
    public String getSubject() { return subject; }
    public Integer getQuantity() { return quantity; }
    public String getDescription() { return description; }
    public String getUrgency() { return urgency; }
    public String getCategory() { return category; }
    public String getGradeLevel() { return gradeLevel; }
    public String getNotes() { return notes; }
    
    // Explicit setters
    public void setTitle(String title) { this.title = title; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setDescription(String description) { this.description = description; }
    public void setUrgency(String urgency) { this.urgency = urgency; }
    public void setCategory(String category) { this.category = category; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }
    public void setNotes(String notes) { this.notes = notes; }
}
