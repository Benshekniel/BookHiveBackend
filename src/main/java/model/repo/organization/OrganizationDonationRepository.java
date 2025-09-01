package model.repo.organization;

import model.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrganizationDonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByOrganizationId(Long orgId);
}
