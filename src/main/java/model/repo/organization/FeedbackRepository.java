package model.repo.organization;

import model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    boolean existsByDonationId(Long donationId);
    Optional<Feedback> findByDonationId(Long donationId);
    List<Feedback> findByOrganizationIdOrderByDateDesc(Long organizationId);

    @Query("SELECT COALESCE(AVG(f.rating), 0.0) FROM Feedback f WHERE f.organizationId = :organizationId")
    Double findAverageRatingByOrganizationId(@Param("organizationId") Long organizationId);
}

//package model.repo.organization;
//
//import model.entity.Feedback;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
//
//    // Find feedback by organization ID, sorted by date descending
//    List<Feedback> findByOrganizationOrgIdOrderByDateDesc(@Param("orgId") Long orgId);
//
//    // Find feedback by donation ID
//    Optional<Feedback> findByDonationId(@Param("donationId") Long donationId);
//
//    // Check if feedback exists for a donation ID
//    boolean existsByDonationId(@Param("donationId") Long donationId);
//
//    // Calculate average feedback rating for an organization
//    @Query("SELECT COALESCE(AVG(f.rating), 0.0) FROM Feedback f WHERE f.organization.orgId = :orgId")
//    Double findAverageRatingByOrgId(@Param("orgId") Long orgId);
//}