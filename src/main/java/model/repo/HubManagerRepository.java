package model.repo;

import model.entity.HubManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HubManagerRepository extends JpaRepository<HubManager, Long> {
    Optional<HubManager> findByUserId(Long userId);
    Optional<HubManager> findByHubId(Long hubId);
    boolean existsByUserId(Long userId);
    List<HubManager> findAllByHubId(Long hubId);
}
