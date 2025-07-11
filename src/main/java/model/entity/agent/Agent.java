package model.entity.agent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agentId;

    @Column(name = "hub_id", nullable = false)
    private Long hubId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String vehicleNumber;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    private Double trustScore = 0.0;

    private Integer deliveryTime;

    private Integer numberOfDelivery = 0;

    public enum VehicleType {
        MOTORCYCLE, BICYCLE, SCOOTER, VAN, CAR
    }

    public enum AvailabilityStatus {
        AVAILABLE, BUSY, OFFLINE
    }
}
