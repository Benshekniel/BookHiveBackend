package model.dto.Delivery;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubSummaryDto {
    private Long hubId;
    private String name;
    private String city;
    private String address;
    private String hubManagerName;
    private Integer totalAgents;
    private Integer availableAgents;
    private Integer todayDeliveries;
    private Integer totalRoutes;
    private BigDecimal monthlyRevenue;
    private String status;
    private LocalDateTime lastUpdated;

    // Quick stats for dashboard display
    private Double efficiencyScore;
    private Boolean needsAttention;
    private String primaryIssue;
}