package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCreateDto {
    private String pickupAddress;
    private String deliveryAddress;
    private Long hubId;
    private Long requesterId;
    private Long transactionId;
    private Long agentId;
}
