// BookRequestRepository.java
package model.repo.organization;

import model.entity.BookRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
    
    // Basic finder methods using organization relationship
    List<BookRequest> findByOrganizationOrgIdOrderByDateRequestedDesc(Long orgId);
    List<BookRequest> findTop5ByOrganizationOrgIdOrderByDateRequestedDesc(Long orgId);
    List<BookRequest> findByOrganizationOrgIdAndStatus(Long orgId, String status);
    List<BookRequest> findByStatus(String status);
    
    // Count methods using organization relationship
    Long countByOrganizationOrgId(Long orgId);
    Long countByOrganizationOrgIdAndStatus(Long orgId, String status);
    Long countByOrganizationOrgIdAndStatusAndDateRequestedBetween(Long orgId, String status, LocalDateTime start, LocalDateTime end);
    
    // Paginated queries
    Page<BookRequest> findByOrganizationOrgId(Long orgId, Pageable pageable);
    Page<BookRequest> findByOrganizationOrgIdAndStatus(Long orgId, String status, Pageable pageable);
    
    // Recent requests query
    @Query("SELECT b FROM BookRequest b WHERE b.organization.orgId = :orgId ORDER BY b.dateRequested DESC")
    List<BookRequest> findRecentByOrganizationId(@Param("orgId") Long orgId, Pageable pageable);
    
    // Default method for compatibility
    default List<BookRequest> findRecentByOrganizationId(Long orgId, int limit) {
        return findRecentByOrganizationId(orgId, Pageable.ofSize(limit));
    }
}