package model.repo;

import model.entity.agent.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    List<Agent> findByHubHubId(Long hubId);
    List<Agent> findByAvailabilityStatus(Agent.AvailabilityStatus status);
    List<Agent> findByHubHubIdAndAvailabilityStatus(Long hubId, Agent.AvailabilityStatus status);
    Optional<Agent> findByUserUserId(Long userId);
    boolean existsByUserUserId(Long userId);
    Long countByHubHubId(Long hubId);
    Long countByHubHubIdAndAvailabilityStatus(Long hubId, Agent.AvailabilityStatus status);

    @Query("SELECT a FROM Agent a WHERE a.trustScore >= :minScore")
    List<Agent> findByTrustScoreGreaterThanEqual(@Param("minScore") Double minScore);
}
