package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "route_boundaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteBoundary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boundary_id")
    private Long boundaryId;

    @Column(name = "route_id", nullable = false)
    private Long routeId;

    @Column(name = "boundary_name")
    private String boundaryName;

    @Column(name = "coordinates", columnDefinition = "TEXT")
    private String coordinates;

    @Column(name = "boundary_type")
    private String boundaryType;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}