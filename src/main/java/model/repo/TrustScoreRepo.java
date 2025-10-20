package model.repo;

import jakarta.transaction.Transactional;
import model.entity.TrustScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface TrustScoreRepo extends JpaRepository<TrustScore, Long> {

    TrustScore findByUserId(Long userId);

    // ✅ Fetch TrustScore by email
    @Query("SELECT t FROM TrustScore t WHERE t.email = :email")
    TrustScore findByEmail(@Param("email") String email);


    // ✅ Update (set) score directly based on email
    @Modifying
    @Query("UPDATE TrustScore t SET t.score = :score WHERE t.email = :email")
    int updateScoreByEmail(@Param("email") String email, @Param("score") Integer score);


    // ✅ Increment (add to) score based on email
    @Modifying
    @Query("UPDATE TrustScore t SET t.score = t.score + :points WHERE t.email = :email")
    int incrementScore(@Param("email") String email, @Param("points") Integer points);


    // ✅ Deduct (subtract) score based on email
    @Modifying
    @Query("UPDATE TrustScore t SET t.score = t.score - :points WHERE t.email = :email AND t.score >= :points")
    int deductScore(@Param("email") String email, @Param("points") Integer points);


    // ✅ Reset score to 0 for a specific user
    @Modifying
    @Query("UPDATE TrustScore t SET t.score = 0 WHERE t.email = :email")
    int resetScore(@Param("email") String email);


    // ✅ Fetch all users with score above a certain threshold
    @Query("SELECT t FROM TrustScore t WHERE t.score > :minScore")
    List<TrustScore> findAllWithScoreAbove(@Param("minScore") Integer minScore);


    // ✅ Fetch top N users by highest trust score
    @Query("SELECT t FROM TrustScore t ORDER BY t.score DESC")
    List<TrustScore> findTopScores(Pageable pageable);


    // ✅ Delete trust score record using email
    @Modifying
    @Query("DELETE FROM TrustScore t WHERE t.email = :email")
    int deleteByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE TrustScore t SET t.score = :newScore WHERE t.email = :email")
    int updateScoreByEmail2(String email, Integer newScore);

}
