package model.repo;

import model.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    List<Agent> findByHubId(Long hubId);
    List<Agent> findByAvailabilityStatus(Agent.AvailabilityStatus status);
    List<Agent> findByHubIdAndAvailabilityStatus(Long hubId, Agent.AvailabilityStatus status);
    Optional<Agent> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    Long countByHubId(Long hubId);
    Long countByHubIdAndAvailabilityStatus(Long hubId, Agent.AvailabilityStatus status);

    @Query("SELECT a FROM Agent a WHERE a.trustScore >= :minScore")
    List<Agent> findByTrustScoreGreaterThanEqual(@Param("minScore") Double minScore);
}
