// FeedbackRepository.java
package model.repo.organization;

import model.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    // Basic finder methods
    List<Feedback> findByOrganizationIdOrderByCreatedAtDesc(Long organizationId);
    List<Feedback> findByDonationId(Long donationId);
    List<Feedback> findByRating(Integer rating);
    
    // Count methods
    int countByOrganizationId(Long organizationId);
    int countByOrganizationIdAndRating(Long organizationId, Integer rating);
    
    // Average calculations
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.organizationId = :orgId")
    Double getAverageRatingByOrganizationId(@Param("orgId") Long organizationId);
    
    // Paginated queries
    Page<Feedback> findByOrganizationId(Long organizationId, Pageable pageable);
    
    // Complex queries
    @Query("SELECT f, o.name, d.donorName FROM Feedback f " +
           "JOIN Organization o ON f.organizationId = o.id " +
           "LEFT JOIN Donation d ON f.donationId = d.id " +
           "WHERE f.organizationId = :orgId ORDER BY f.createdAt DESC")
    List<Object[]> findFeedbackWithDetailsForOrganization(@Param("orgId") Long organizationId);
    
    @Query("SELECT o.id, o.name, AVG(f.rating) as avgRating, COUNT(f) as feedbackCount " +
           "FROM Feedback f JOIN Organization o ON f.organizationId = o.id " +
           "GROUP BY o.id, o.name ORDER BY avgRating DESC")
    List<Object[]> getOrganizationRankingByFeedback();
    
    @Query("SELECT f.rating, COUNT(f) FROM Feedback f " +
           "WHERE f.organizationId = :orgId GROUP BY f.rating ORDER BY f.rating DESC")
    List<Object[]> getRatingDistribution(@Param("orgId") Long organizationId);
}