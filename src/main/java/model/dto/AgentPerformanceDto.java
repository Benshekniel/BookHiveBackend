package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentPerformanceDto {
    private Long agentId;
    private String agentName;
    private String name;
    private Long totalDeliveries;
    private Long successfulDeliveries;
    private Long failedDeliveries;
    private Double successRate;
    private Double avgDeliveryTime;
    private Double trustScore;
    private Integer deliveries;
    private Integer avgTime;
    private double rating;
}
