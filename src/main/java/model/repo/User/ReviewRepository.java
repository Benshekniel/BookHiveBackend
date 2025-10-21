package model.repo.User;

import model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookId(Long bookId);

    List<Review> findByReviewerUserId(Long userId);

    List<Review> findByReviewedUserId(Long userId);

    List<Review> findByTransactionId(Long transactionId);

    // ✅ CRITICAL: Check for duplicate reviews by same user for same transaction
    Optional<Review> findByTransactionIdAndReviewerUserId(Long transactionId, Long reviewerUserId);

    @Query("SELECT AVG(r.sellerRating) FROM Review r WHERE r.reviewedUserId = :userId")
    Double getAverageSellerRating(@Param("userId") Long userId);

    @Query("SELECT AVG(r.bookConditionRating) FROM Review r WHERE r.bookId = :bookId")
    Double getAverageBookConditionRating(@Param("bookId") Long bookId);

    @Query("SELECT AVG(r.overallRating) FROM Review r WHERE r.bookId = :bookId")
    Double getAverageBookRating(@Param("bookId") Long bookId);

    // ✅ Additional useful queries
    @Query("SELECT AVG(r.overallRating) FROM Review r WHERE r.reviewedUserId = :userId")
    Double getAverageOverallRating(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.transactionId = :transactionId")
    Long countReviewsByTransactionId(@Param("transactionId") Long transactionId);
}