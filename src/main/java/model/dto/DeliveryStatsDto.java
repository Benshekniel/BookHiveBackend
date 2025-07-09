package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import model.entity.delivery.Delivery;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatsDto {
    private Long totalDeliveries;
    private Long pendingDeliveries;
    private Long inTransitDeliveries;
    private Long deliveredDeliveries;
    private Long failedDeliveries;
    private Double successRate;
    private Double avgDeliveryTime;
    private Delivery.DeliveryStatus status;
    private Long count;
    
    public DeliveryStatsDto(Delivery.DeliveryStatus status, long count) {
        this.status = status;
        this.count = count;
    }
}
