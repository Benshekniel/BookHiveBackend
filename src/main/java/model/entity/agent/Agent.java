package model.entity.agent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.entity.User;
import model.entity.delivery.Hub;

@Entity
@Table(name = "agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
