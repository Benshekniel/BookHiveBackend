package model.repo;


import jakarta.transaction.Transactional;
import model.entity.TrustScoreRegulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface TrustScoreRegulationRepository extends JpaRepository<TrustScoreRegulation, Long> {

    // Retrieve the amount by rule name
    @Query("SELECT t.amount FROM TrustScoreRegulation t WHERE t.rule = :rule")
    Integer findAmountByRule(String rule);


    // 🔹 Update each rule's amount
    @Modifying
    @Transactional
    @Query("UPDATE TrustScoreRegulation t SET t.amount = :amount WHERE t.rule = 'REVIEW'")
    int updateReviewAmount(Integer amount);

    @Modifying
    @Transactional
    @Query("UPDATE TrustScoreRegulation t SET t.amount = :amount WHERE t.rule = 'PURCHASE'")
    int updatePurchaseAmount(Integer amount);

    @Modifying
    @Transactional
    @Query("UPDATE TrustScoreRegulation t SET t.amount = :amount WHERE t.rule = 'COMPJOIN'")
    int updateCompJoinAmount(Integer amount);

    @Modifying
    @Transactional
    @Query("UPDATE TrustScoreRegulation t SET t.amount = :amount WHERE t.rule = 'NEGATIVE'")
    int updateNegativeAmount(Integer amount);

    @Modifying
    @Transactional
    @Query("UPDATE TrustScoreRegulation t SET t.amount = :amount WHERE t.rule = 'POSITIVE'")
    int updatePositiveAmount(Integer amount);

}
