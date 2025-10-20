package model.repo.Delivery;

import model.entity.RouteAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RouteAssignmentRepository extends JpaRepository<RouteAssignment, Long> {

    List<RouteAssignment> findByRouteId(Long routeId);
    List<RouteAssignment> findByAgentId(Long agentId);
    List<RouteAssignment> findByStatus(RouteAssignment.AssignmentStatus status);

    List<RouteAssignment> findByRouteIdAndStatus(Long routeId, RouteAssignment.AssignmentStatus status);
    List<RouteAssignment> findByAgentIdAndStatus(Long agentId, RouteAssignment.AssignmentStatus status);

    List<RouteAssignment> findByRouteIdAndAgentIdAndStatus(Long routeId, Long agentId, RouteAssignment.AssignmentStatus status);

    boolean existsByRouteIdAndAgentIdAndStatus(Long routeId, Long agentId, RouteAssignment.AssignmentStatus status);

    void deleteByRouteId(Long routeId);
    void deleteByAgentId(Long agentId);

    @Query("SELECT COUNT(ra) FROM RouteAssignment ra WHERE ra.routeId = :routeId AND ra.status = 'ACTIVE'")
    Long countActiveAssignmentsByRoute(@Param("routeId") Long routeId);

    @Query("SELECT COUNT(ra) FROM RouteAssignment ra WHERE ra.agentId = :agentId AND ra.status = 'ACTIVE'")
    Long countActiveAssignmentsByAgent(@Param("agentId") Long agentId);
}