// DonationRepository.java
package model.repo.organization;

import model.entity.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    
    // Basic finder methods
    List<Donation> findByOrganizationIdOrderByDonationDateDesc(Long organizationId);
    List<Donation> findByOrganizationIdAndStatusOrderByDonationDateDesc(Long organizationId, String status);
    List<Donation> findByDonorNameContainingIgnoreCase(String donorName);
    List<Donation> findByStatus(String status);
    
    // Count methods
    int countByOrganizationId(Long organizationId);
    int countByOrganizationIdAndStatus(Long organizationId, String status);
    
    // Value aggregation methods
    @Query("SELECT SUM(d.value) FROM Donation d WHERE d.organizationId = :orgId")
    Double sumValueByOrganizationId(@Param("orgId") Long organizationId);
    
    @Query("SELECT SUM(d.value) FROM Donation d WHERE d.organizationId = :orgId AND d.status = :status")
    Double sumValueByOrganizationIdAndStatus(@Param("orgId") Long organizationId, @Param("status") String status);
    
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.organizationId = :orgId")
    Double getAverageFeedbackRating(@Param("orgId") Long organizationId);
    
    // Paginated queries
    Page<Donation> findByOrganizationId(Long organizationId, Pageable pageable);
    Page<Donation> findByOrganizationIdAndStatus(Long organizationId, String status, Pageable pageable);
    
    // Complex queries
    @Query("SELECT d, o.name FROM Donation d JOIN Organization o ON d.organizationId = o.id " +
           "WHERE d.status = :status ORDER BY d.donationDate DESC")
    List<Object[]> findDonationsWithOrganizationNameByStatus(@Param("status") String status);
    
    @Query("SELECT d FROM Donation d WHERE d.donationType = :type AND d.value >= :minValue")
    List<Donation> findByDonationTypeAndMinValue(@Param("type") String type, @Param("minValue") Double minValue);
    
    @Query("SELECT d FROM Donation d WHERE d.organizationId = :orgId AND " +
           "d.donationDate BETWEEN :startDate AND :endDate")
    List<Donation> findByOrganizationIdAndDateRange(
            @Param("orgId") Long organizationId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT d.donationType, SUM(d.quantity) FROM Donation d " +
           "WHERE d.organizationId = :orgId GROUP BY d.donationType")
    List<Object[]> getDonationTypeStatistics(@Param("orgId") Long organizationId);
}