package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubStatsDto {
    private Long hubId;
    private String hubName;
    private String city;
    private Long totalAgents;
    private Long activeAgents;
    private Long totalDeliveries;
    private Long todayDeliveries;
}
