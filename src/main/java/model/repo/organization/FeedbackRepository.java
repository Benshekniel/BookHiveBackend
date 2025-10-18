package model.repo.organization;

import model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByOrganizationIdOrderByDateDesc(Long organizationId);

    Optional<Feedback> findByDonationId(Long donationId);

    boolean existsByDonationId(Long donationId);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.organization.id = :organizationId")
    Double getAverageRatingByOrganizationId(@Param("organizationId") Long organizationId);
}