package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubPerformanceDto {
    private Long hubId;
    private String hubName;
    private Long totalDeliveries;
    private Long successfulDeliveries;
    private Long failedDeliveries;
    private Double successRate;
    private Double avgDeliveryTime;
    private Double efficiencyScore;
}
