// AgentRepository.java - Complete repository with optimized queries
package model.repo.Hubmanager;

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

    // Optimized query to get all agents with user and hub details in one query
    @Query("SELECT a,  u.name as userName, u.email as userEmail, u.phoneNumber as userPhone, " +
            "h.name as hubName, " +
            "(SELECT COUNT(d) FROM Delivery d WHERE d.agentId = a.agentId) as totalDeliveries " +
            "FROM Agent a " +
            "LEFT JOIN AllUsers u ON a.userId = u.user_id " +
            "LEFT JOIN Hub h ON a.hubId = h.hubId")
    List<Object[]> findAllAgentsWithDetails();

    // Query to get single agent with all details
    @Query("SELECT a, u.name as userName, u.email as userEmail, u.phoneNumber as userPhone, " +
            "h.name as hubName, " +
            "(SELECT COUNT(d) FROM Delivery d WHERE d.agentId = a.agentId) as totalDeliveries " +
            "FROM Agent a " +
            "LEFT JOIN AllUsers u ON a.userId = u.user_id " +
            "LEFT JOIN Hub h ON a.hubId = h.hubId " +
            "WHERE a.agentId = :agentId")
    Optional<Object[]> findAgentWithDetails(@Param("agentId") Long agentId);

    // Query to get agents by hub with details
    @Query("SELECT a, u.name as userName, u.email as userEmail, u.phoneNumber as userPhone, " +
            "h.name as hubName, " +
            "(SELECT COUNT(d) FROM Delivery d WHERE d.agentId = a.agentId) as totalDeliveries " +
            "FROM Agent a " +
            "LEFT JOIN AllUsers u ON a.userId = u.user_id " +
            "LEFT JOIN Hub h ON a.hubId = h.hubId " +
            "WHERE a.hubId = :hubId")
    List<Object[]> findAgentsByHubWithDetails(@Param("hubId") Long hubId);
}