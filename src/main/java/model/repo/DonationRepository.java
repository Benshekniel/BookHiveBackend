package model.repo;

import model.entity.Donation;
import model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByRecipientOrganization(Organization organization);

    List<Donation> findByRecipientOrganizationAndStatus(Organization organization, Donation.DonationStatus status);

    @Query("SELECT COUNT(d) FROM Donation d WHERE d.recipientOrganization = :org AND d.status = :status")
    long countByRecipientOrganizationAndStatus(@Param("org") Organization organization, @Param("status") Donation.DonationStatus status);

    @Query("SELECT COUNT(d) FROM Donation d WHERE d.recipientOrganization = :org")
    long countByRecipientOrganization(@Param("org") Organization organization);

    List<Donation> findByRecipientOrganizationOrderByCreatedAtDesc(Organization organization);

    @Query("SELECT d FROM Donation d WHERE d.recipientOrganization = :org AND " +
           "(:status = 'all' OR d.status = :status) " +
           "ORDER BY d.createdAt DESC")
    List<Donation> findByRecipientOrganizationAndStatusFilterOrderByCreatedAtDesc(
            @Param("org") Organization organization, 
            @Param("status") String status);

    @Query("SELECT SUM(d.quantity) FROM Donation d WHERE d.recipientOrganization = :org AND d.status = 'RECEIVED'")
    Long getTotalBooksReceivedByOrganization(@Param("org") Organization organization);
}
