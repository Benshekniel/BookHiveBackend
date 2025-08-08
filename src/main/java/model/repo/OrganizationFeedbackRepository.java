package model.repo;

import model.entity.OrganizationFeedback;
import model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationFeedbackRepository extends JpaRepository<OrganizationFeedback, Long> {

    List<OrganizationFeedback> findByOrganization(Organization organization);

    List<OrganizationFeedback> findByOrganizationOrderBySubmittedDateDesc(Organization organization);

    List<OrganizationFeedback> findByOrganizationAndStatus(Organization organization, OrganizationFeedback.FeedbackStatus status);
}
