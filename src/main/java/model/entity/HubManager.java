package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.entity.delivery.Hub;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }
}
