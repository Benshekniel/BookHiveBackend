package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "hub_id", nullable = false)
    private Long hubId;

    @Column(name = "coverage_area")
    private String coverageArea; // e.g., "15.2 km²"

    @Column(name = "postal_codes", columnDefinition = "TEXT")
    private String postalCodes; // Comma-separated postal codes

    @Enumerated(EnumType.STRING)
    private RouteStatus status;

    @Column(name = "center_latitude")
    private Double centerLatitude;

    @Column(name = "center_longitude")
    private Double centerLongitude;

    @Column(name = "boundary_coordinates", columnDefinition = "TEXT")
    private String boundaryCoordinates; // JSON string of boundary coordinates

    @Column(name = "estimated_delivery_time")
    private Integer estimatedDeliveryTime; // in minutes

    @Column(name = "max_daily_deliveries")
    private Integer maxDailyDeliveries;

    @Column(name = "priority_level")
    private Integer priorityLevel; // 1-5, with 1 being highest priority

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy; // User ID who created the route

    // Route boundaries and areas
    @Column(name = "neighborhoods", columnDefinition = "TEXT")
    private String neighborhoods; // JSON array of neighborhood names

    @Column(name = "landmarks", columnDefinition = "TEXT")
    private String landmarks; // JSON array of notable landmarks

    @Column(name = "traffic_pattern")
    @Enumerated(EnumType.STRING)
    private TrafficPattern trafficPattern;

    @Column(name = "route_type")
    @Enumerated(EnumType.STRING)
    private RouteType routeType;

    @Column(name = "vehicle_restrictions", columnDefinition = "TEXT")
    private String vehicleRestrictions; // JSON array of allowed vehicle types

    // Performance metrics (calculated fields)
    @Transient
    private Integer dailyDeliveries;

    @Transient
    private Integer assignedRiders;

    @Transient
    private Integer totalRiders;

    @Transient
    private Double averageDeliveryTime;

    @Transient
    private List<Agent> agents;

    public enum RouteStatus {
        ACTIVE, INACTIVE, MAINTENANCE, SUSPENDED
    }

    public enum TrafficPattern {
        LOW, MODERATE, HIGH, VARIABLE
    }

    public enum RouteType {
        RESIDENTIAL, COMMERCIAL, INDUSTRIAL, MIXED, UNIVERSITY, DOWNTOWN
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = RouteStatus.ACTIVE;
        }
        if (priorityLevel == null) {
            priorityLevel = 3; // Default to medium priority
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public String getFormattedCoverageArea() {
        return coverageArea != null ? coverageArea : "N/A";
    }

    public String getFormattedEstimatedTime() {
        return estimatedDeliveryTime != null ? estimatedDeliveryTime + " min" : "N/A";
    }

    public boolean isActive() {
        return status == RouteStatus.ACTIVE;
    }

    public String getPostalCodesList() {
        return postalCodes != null ? postalCodes : "";
    }

    // Convert postal codes string to array
    public String[] getPostalCodesArray() {
        if (postalCodes == null || postalCodes.trim().isEmpty()) {
            return new String[0];
        }
        return postalCodes.split(",\\s*");
    }

    // Set postal codes from array
    public void setPostalCodesFromArray(String[] codes) {
        if (codes != null && codes.length > 0) {
            this.postalCodes = String.join(", ", codes);
        } else {
            this.postalCodes = null;
        }
    }
}