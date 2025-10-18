package model.dto.organization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpcomingEventsDTO {
    private Long id;
    private String title;
    private String date;
    private String time;
    private String location;
    private String description;
}