package model.repo.organization;

import model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    // Find organization by email
    Optional<Organization> findByEmail(@Param("email") String email);

    // Find organization by registration number
    Optional<Organization> findByRegNo(@Param("regNo") String regNo);

    // Count book requests for an organization
    @Query("SELECT COUNT(br) FROM BookRequest br WHERE br.organization.orgId = :orgId")
    Long countBookRequestsByOrgId(@Param("orgId") Long orgId);

    // Count donations with status RECEIVED for an organization
    @Query("SELECT COUNT(d) FROM Donation d WHERE d.organizationId = :orgId AND d.status = 'RECEIVED'")
    Long countDonationsReceivedByOrgId(@Param("orgId") Long orgId);

    // Calculate average feedback rating for an organization
    @Query("SELECT COALESCE(AVG(f.rating), 0.0) FROM Feedback f WHERE f.organizationId = :orgId")
    Double findAverageRatingByOrgId(@Param("orgId") Long orgId);
}