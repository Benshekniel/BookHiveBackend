package model.repo;

import model.entity.OrganizationFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationFeedbackRepository extends JpaRepository<OrganizationFeedback, Long> {
	List<OrganizationFeedback> findByOrganizationId(Long organizationId);
}
