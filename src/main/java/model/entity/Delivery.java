package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    @Column(name = "route_id")
    private Long routeId;

//    @Column(name = "agent_id")
//    private Long agentId;

    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "user_id")
    private Long userId;

    private LocalDateTime createdAt;

    // New fields
    @Column(name = "weight")
    private String weight; // e.g., "2.5 kg", "500 g"

    @Column(name = "dimensions")
    private String dimensions; // e.g., "30x20x15 cm"

    @Column(name = "value", precision = 10, scale = 2)
    private BigDecimal value; // Monetary value with precision

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "delivery_notes", columnDefinition = "TEXT")
    private String deliveryNotes; // Additional notes for delivery


    public enum DeliveryStatus {
        PENDING, ASSIGNED, PICKED_UP, IN_TRANSIT, DELIVERED, FAILED, CANCELLED, DELAYED
    }

    public enum PaymentMethod {
        CASH, CREDIT_CARD, DEBIT_CARD, DIGITAL_WALLET, BANK_TRANSFER, CHEQUE, COD
    }

    public enum Priority {
        LOW, NORMAL, HIGH, URGENT
    }

    // Helper methods for better usability
    public String getFormattedValue() {
        return value != null ? "$" + value.toString() : "$0.00";
    }

    public boolean isCompleted() {
        return status == DeliveryStatus.DELIVERED;
    }

    public boolean isPending() {
        return status == DeliveryStatus.PENDING || status == DeliveryStatus.ASSIGNED;
    }
}