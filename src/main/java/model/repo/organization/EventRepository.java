// EventRepository.java
package model.repo.organization;

import model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    int countByOrganizationId(Long organizationId);
    
    @Query("SELECT e FROM Event e WHERE e.organizationId = :orgId AND e.eventDate > :now ORDER BY e.eventDate ASC")
    List<Event> findUpcomingEventsByOrganizationId(@Param("orgId") Long organizationId, @Param("now") LocalDateTime now);
}