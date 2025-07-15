package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @Column(columnDefinition = "TEXT")
    private String pickupAddress;

    @Column(columnDefinition = "TEXT")
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private LocalDateTime pickupTime;

    private LocalDateTime deliveryTime;

    private String trackingNumber;

    @Column(name = "hub_id")
    private Long hubId;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "transaction_id")
    private Long transactionId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum DeliveryStatus {
        PENDING, ASSIGNED, PICKED_UP, IN_TRANSIT, DELIVERED, FAILED, CANCELLED
    }
}