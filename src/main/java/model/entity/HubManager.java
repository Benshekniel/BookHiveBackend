package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "hub_managers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hubManagerId;

    @Column(name = "hub_id", nullable = false)
    private Long hubId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }
}
