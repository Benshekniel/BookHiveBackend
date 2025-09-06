package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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

    @Column(name = "phone_number", length = 255)
    private String phoneNumber;

    private Integer numberOfDelivery = 0;

    // New columns added for better tracking
    @Column(name = "application_id")
    private Long applicationId; // Reference to the original application

    @Column(name = "joined_date")
    private LocalDateTime joinedDate = LocalDateTime.now();

    @Column(name = "last_active")
    private LocalDateTime lastActive;

    @Column(name = "emergency_contact", length = 255)
    private String emergencyContact;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "vehicle_insurance_expiry")
    private LocalDateTime vehicleInsuranceExpiry;

    @Column(name = "license_expiry")
    private LocalDateTime licenseExpiry;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum VehicleType {
        MOTORCYCLE, BICYCLE, SCOOTER, VAN, CAR
    }

    public enum AvailabilityStatus {
        AVAILABLE, BUSY, UNAVAILABLE, OFFLINE
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}