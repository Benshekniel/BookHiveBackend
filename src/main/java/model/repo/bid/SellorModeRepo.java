package model.repo.bid;


import model.entity.Bid.SellorMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface SellorModeRepo extends JpaRepository<SellorMode, Integer> {

    // Fetch SellorMode by user_id

    // Optional, works fine
    @Query("SELECT s FROM SellorMode s WHERE s.userId = :userId")
    SellorMode findByUserIdCustom(@Param("userId") int userId);

    // Preferred derived query (uses Java property name userId)
    Optional<SellorMode> findByUserId(int userId);

// REMOVE this — it causes the startup error
// SellorMode findByUser_id(int user_id);
}
