package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import model.entity.delivery.Delivery;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {
    private Long deliveryId;
    private String pickupAddress;
    private String deliveryAddress;
    private Delivery.DeliveryStatus status;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryTime;
    private String trackingNumber;
    private LocalDateTime createdAt;
    private Long hubId;
    private String hubName;
    private Long agentId;
    private String agentName;
    private String customerName;
    private String customerPhone;
    private Long transactionId;
    private String bookTitle;
    private String bookAuthor;
}
