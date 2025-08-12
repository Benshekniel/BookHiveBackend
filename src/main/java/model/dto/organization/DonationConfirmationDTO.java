package model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationConfirmationDTO {
    public String getReceivedCondition() { return receivedCondition; }
    public void setReceivedCondition(String receivedCondition) { this.receivedCondition = receivedCondition; }

    public Integer getActualQuantityReceived() { return actualQuantityReceived; }
    public void setActualQuantityReceived(Integer actualQuantityReceived) { this.actualQuantityReceived = actualQuantityReceived; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getDateReceived() { return dateReceived; }
    public void setDateReceived(LocalDateTime dateReceived) { this.dateReceived = dateReceived; }

    public boolean isAllItemsReceived() { return allItemsReceived; }
    public void setAllItemsReceived(boolean allItemsReceived) { this.allItemsReceived = allItemsReceived; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    private String receivedCondition;
    private Integer actualQuantityReceived;
    private String notes;
    private LocalDateTime dateReceived;
    private boolean allItemsReceived;
    private String feedback;
}
