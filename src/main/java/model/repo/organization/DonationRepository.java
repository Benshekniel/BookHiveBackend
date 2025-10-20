package model.repo.organization;

import model.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    // Find donations by organization ID, sorted by dateDonated descending
    List<Donation> findByOrganizationIdOrderByDateDonatedDesc(Long organizationId);

    // Find donations by organization ID and status with no feedback
    @Query("SELECT d FROM Donation d WHERE d.organizationId = :organizationId AND d.status = :status AND NOT EXISTS (SELECT f FROM Feedback f WHERE f.donationId = d.id)")
    List<Donation> findByOrganizationIdAndStatusAndNoFeedback(@Param("organizationId") Long organizationId, @Param("status") String status);

    // Count donations by organization ID
    Long countByOrganizationId(Long organizationId);

    // Count donations by organization ID and status
    Long countByOrganizationIdAndStatus(Long organizationId, String status);

    // Sum the quantity of donations by organization ID and status
    @Query("SELECT COALESCE(SUM(d.quantity), 0) FROM Donation d WHERE d.organizationId = :organizationId AND d.status = :status")
    Long countBooksByOrganizationIdAndStatus(@Param("organizationId") Long organizationId, @Param("status") String status);

    // Sum the quantity of donations by organization ID, status, and dateReceived range
    @Query("SELECT COALESCE(SUM(d.quantity), 0) FROM Donation d WHERE d.organizationId = :organizationId AND d.status = :status AND d.dateReceived BETWEEN :start AND :end")
    Long countBooksByOrganizationIdAndStatusAndDateReceivedBetween(
            @Param("organizationId") Long organizationId,
            @Param("status") String status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Count donations by organization ID and dateDonated range
    @Query("SELECT COALESCE(COUNT(d), 0) FROM Donation d WHERE d.organizationId = :organizationId AND d.dateDonated BETWEEN :start AND :end")
    Long countByOrganizationIdAndDateDonatedBetween(
            @Param("organizationId") Long organizationId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Added for FeedbackServiceImpl
    Optional<Donation> findById(Long id);

    // Find donation by book request ID
    Optional<Donation> findByBookRequestId(Long bookRequestId);

    // Delete donation by book request ID
    void deleteByBookRequestId(Long bookRequestId);
}