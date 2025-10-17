package model.repo.organization;

import model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByEmail(String email);
    
    Optional<Organization> findById(Long id);
    
    boolean existsById(Long id);
    
    @Query("SELECT COUNT(br) FROM BookRequest br WHERE br.organization.id = :organizationId")
    Long countBookRequestsByOrganizationId(@Param("organizationId") Long organizationId);

    @Query("SELECT COUNT(d) FROM Donation d WHERE d.organization.id = :organizationId AND d.status = 'RECEIVED'")
    Long countDonationsReceivedByOrganizationId(@Param("organizationId") Long organizationId);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.organization.id = :organizationId")
    Double getAverageFeedbackRatingByOrganizationId(@Param("organizationId") Long organizationId);
}