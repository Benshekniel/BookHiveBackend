package model.dto.Delivery;

import model.dto.Delivery.AgentDto.AgentResponseDto;
import model.dto.Delivery.DeliveryDto.DeliveryResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubDetailedDataDto {
    private Long hubId;
    private String hubName;
    private PerformanceDto performance;
    private List<AgentResponseDto> agents;
    private List<DeliveryResponseDto> recentDeliveries;
    private LocalDateTime timestamp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceDto {
        private Long totalDeliveries;
        private Long successfulDeliveries;
        private Long failedDeliveries;
        private Double successRate;
        private Double avgDeliveryTime;
        private Double efficiencyScore;
    }
}