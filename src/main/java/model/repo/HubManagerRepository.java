package model.repo;

import model.entity.HubManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HubManagerRepository extends JpaRepository<HubManager, Long> {
    Optional<HubManager> findByUserUserId(Long userId);
    Optional<HubManager> findByHubHubId(Long hubId);
    boolean existsByUserUserId(Long userId);
    List<HubManager> findAllByHubHubId(Long hubId);
}
