package model.dto.Delivery;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverySummaryDto {
    private Integer totalDeliveries;
    private Integer activeDeliveries;
    private Integer completedDeliveries;
    private Integer pendingDeliveries;
    private Integer failedDeliveries;
    private Double successRate;
    private Double avgDeliveryTime;
    private Map<String, Integer> statusBreakdown;
    private LocalDateTime timestamp;
}