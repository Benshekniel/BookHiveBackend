package model.repo.organization;

import model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrganizationFeedbackRepository extends JpaRepository<Review, Long> {
    List<Review> findByOrganizationId(Long orgId);
}
