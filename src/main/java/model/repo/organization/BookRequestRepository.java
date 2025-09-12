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
    
    // Basic finder methods
    List<BookRequest> findByOrganizationIdOrderByCreatedAtDesc(Long organizationId);
    List<BookRequest> findTop5ByOrganizationIdOrderByCreatedAtDesc(Long organizationId);
    List<BookRequest> findByOrganizationIdAndStatus(Long organizationId, String status);
    List<BookRequest> findByStatus(String status);
    
    // Count methods
    int countByOrganizationId(Long organizationId);
    int countByOrganizationIdAndStatus(Long organizationId, String status);
    
    // Paginated queries
    Page<BookRequest> findByOrganizationId(Long organizationId, Pageable pageable);
    Page<BookRequest> findByOrganizationIdAndStatus(Long organizationId, String status, Pageable pageable);
    
    // Complex queries
    @Query("SELECT b, o.name FROM BookRequest b JOIN Organization o ON b.organizationId = o.id " +
           "WHERE b.status = :status ORDER BY b.createdAt DESC")
    List<Object[]> findRequestsWithOrganizationNameByStatus(@Param("status") String status);
    
    @Query("SELECT b, o.name FROM BookRequest b JOIN Organization o ON b.organizationId = o.id " +
           "WHERE b.createdAt >= :startDate ORDER BY b.createdAt DESC")
    List<Object[]> findRecentRequestsWithOrganizationName(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT DISTINCT category FROM BookRequest b JOIN b.categories category")
    List<String> findAllDistinctCategories();
    
    @Query("SELECT b FROM BookRequest b WHERE :category MEMBER OF b.categories")
    List<BookRequest> findByCategory(@Param("category") String category);
    
    @Query("SELECT b FROM BookRequest b WHERE b.organizationId = :orgId AND " +
           "(:category IS NULL OR :category MEMBER OF b.categories)")
    List<BookRequest> findByOrganizationIdAndCategory(
            @Param("orgId") Long organizationId,
            @Param("category") String category);
}