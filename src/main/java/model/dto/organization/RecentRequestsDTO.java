package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecentRequestsDTO {
    private Long id;
    private String title;
    private String subject;
    private Integer quantity;
    private String status;
    private String dateRequested;
}
