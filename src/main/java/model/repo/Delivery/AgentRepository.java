package model.repo.Delivery;

import model.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    // Basic finder methods
    List<Agent> findByHubId(Long hubId);
    List<Agent> findByAvailabilityStatus(Agent.AvailabilityStatus status);
    List<Agent> findByHubIdAndAvailabilityStatus(Long hubId, Agent.AvailabilityStatus status);
    Optional<Agent> findByUserId(Long userId);
    boolean existsByUserId(Long userId);

    // Count methods
    Long countByHubId(Long hubId);
    Long countByHubIdAndAvailabilityStatus(Long hubId, Agent.AvailabilityStatus status);

    @Query("SELECT COUNT(a) FROM Agent a WHERE a.availabilityStatus = :status")
    Long countByAvailabilityStatus(@Param("status") Agent.AvailabilityStatus status);

    // Complex queries with details
    @Query("SELECT a FROM Agent a WHERE a.trustScore >= :minScore")
    List<Agent> findByTrustScoreGreaterThanEqual(@Param("minScore") Double minScore);

    @Query("SELECT a, u.name, u.email, a.phoneNumber, h.name, " +
            "(SELECT COUNT(d) FROM Delivery d " +
            "JOIN Route r ON d.routeId = r.routeId " +
            "JOIN RouteAssignment ra ON r.routeId = ra.routeId " +
            "WHERE ra.agentId = a.agentId) " +
            "FROM Agent a " +
            "LEFT JOIN AllUsers u ON a.userId = u.user_id " +
            "LEFT JOIN Hub h ON a.hubId = h.hubId")
    List<Object[]> findAllAgentsWithDetails();

    @Query("SELECT a, u.name, u.email, a.phoneNumber, h.name, " +
            "(SELECT COUNT(d) FROM Delivery d " +
            "JOIN Route r ON d.routeId = r.routeId " +
            "JOIN RouteAssignment ra ON r.routeId = ra.routeId " +
            "WHERE ra.agentId = a.agentId) " +
            "FROM Agent a " +
            "LEFT JOIN AllUsers u ON a.userId = u.user_id " +
            "LEFT JOIN Hub h ON a.hubId = h.hubId " +
            "WHERE a.agentId = :agentId")
    Optional<Object[]> findAgentWithDetails(@Param("agentId") Long agentId);

    @Query("SELECT a, u.name, u.email, a.phoneNumber, h.name, " +
            "(SELECT COUNT(d) FROM Delivery d " +
            "JOIN Route r ON d.routeId = r.routeId " +
            "JOIN RouteAssignment ra ON r.routeId = ra.routeId " +
            "WHERE ra.agentId = a.agentId) " +
            "FROM Agent a " +
            "LEFT JOIN AllUsers u ON a.userId = u.user_id " +
            "LEFT JOIN Hub h ON a.hubId = h.hubId " +
            "WHERE a.hubId = :hubId")
    List<Object[]> findAgentsByHubWithDetails(@Param("hubId") Long hubId);
}