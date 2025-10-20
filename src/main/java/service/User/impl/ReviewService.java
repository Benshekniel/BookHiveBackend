package service.User.impl;

import model.dto.User.ReviewSubmissionDto;
import model.entity.Review;
import model.entity.Transaction;
import model.entity.Delivery;
import model.repo.User.ReviewRepository;
import model.repo.User.UserTransactionRepository;
import model.repo.Delivery.DeliveryRepository; // ✅ Updated import path
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserTransactionRepository transactionRepository;

    // ✅ Add DeliveryRepository with correct import
    @Autowired
    private DeliveryRepository deliveryRepository;

    /**
     * Submit a new review for a transaction
     */
    public Review submitReview(ReviewSubmissionDto reviewDto) {
        System.out.println("=== REVIEW SERVICE DEBUG ===");
        System.out.println("Received DTO: " + reviewDto.toString());
        System.out.println("============================");

        // Use DTO's built-in validation
        if (!reviewDto.isValid()) {
            String error = reviewDto.getValidationError();
            System.err.println("Validation error: " + error);
            throw new IllegalArgumentException(error);
        }

        // Validate transaction exists
        Optional<Transaction> transactionOpt = transactionRepository.findById(reviewDto.getTransactionId());
        if (!transactionOpt.isPresent()) {
            String error = "Transaction not found with ID: " + reviewDto.getTransactionId();
            System.err.println(error);
            throw new RuntimeException(error);
        }

        Transaction transaction = transactionOpt.get();
        System.out.println("Found transaction: " + transaction.getTransactionId() + " Type: " + transaction.getType() + " Status: " + transaction.getStatus());

        // Check if user is involved in the transaction
        if (!isUserInvolvedInTransaction(reviewDto.getReviewerUserId(), transaction)) {
            String error = "User " + reviewDto.getReviewerUserId() + " not authorized to review this transaction";
            System.err.println(error);
            throw new RuntimeException(error);
        }

        // Check if THIS USER already reviewed THIS TRANSACTION
        Optional<Review> existingReview = reviewRepository.findByTransactionIdAndReviewerUserId(
                reviewDto.getTransactionId(),
                reviewDto.getReviewerUserId()
        );

        if (existingReview.isPresent()) {
            String error = "You have already submitted a review for this transaction";
            System.err.println(error);
            throw new RuntimeException(error);
        }

        // ✅ CRITICAL FIX: Check delivery status using your existing repository
        if (!isTransactionEligibleForReview(transaction)) {
            String error = "Transaction must be completed or delivered to submit a review";
            System.err.println(error);
            throw new RuntimeException(error);
        }

        // Determine who is being reviewed based on transaction type
        Long reviewedUserId = determineReviewedUser(transaction, reviewDto.getReviewerUserId());
        if (reviewedUserId == null) {
            System.out.println("Warning: Could not determine reviewed user, proceeding without it");
        }

        // Create new review
        Review review = new Review();
        review.setTransactionId(reviewDto.getTransactionId());
        review.setBookId(reviewDto.getBookId());
        review.setReviewerUserId(reviewDto.getReviewerUserId());
        review.setReviewedUserId(reviewedUserId);
        review.setSellerRating(reviewDto.getSellerRating());
        review.setBookConditionRating(reviewDto.getBookConditionRating());
        review.setOverallRating(reviewDto.getOverallRating());
        review.setReviewComment(reviewDto.getReviewComment());

        System.out.println("Saving review for transaction: " + reviewDto.getTransactionId());
        System.out.println("Review entity before save: " + review);

        Review savedReview = reviewRepository.save(review);
        System.out.println("Review saved successfully with ID: " + savedReview.getReviewId());
        System.out.println("=== END REVIEW SERVICE DEBUG ===");

        return savedReview;
    }

    /**
     * ✅ UPDATED: Check if transaction is eligible for review by checking delivery status
     * Works with your existing DeliveryRepository that returns List<Delivery>
     */
    private boolean isTransactionEligibleForReview(Transaction transaction) {
        System.out.println("=== CHECKING REVIEW ELIGIBILITY ===");
        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("Transaction Status: " + transaction.getStatus());

        // ✅ Use your existing method that returns List<Delivery>
        List<Delivery> deliveries = deliveryRepository.findByTransactionId(transaction.getTransactionId());

        if (deliveries != null && !deliveries.isEmpty()) {
            System.out.println("Found " + deliveries.size() + " delivery record(s) for transaction " + transaction.getTransactionId());

            // ✅ Check if ANY delivery is completed (DELIVERED status)
            for (Delivery delivery : deliveries) {
                System.out.println("Delivery ID: " + delivery.getDeliveryId() + ", Status: " + delivery.getStatus());

                if (delivery.getStatus() == Delivery.DeliveryStatus.DELIVERED) {
                    System.out.println("✅ Found DELIVERED delivery - Transaction eligible for review");
                    System.out.println("=== END REVIEW ELIGIBILITY CHECK ===");
                    return true;
                }
            }

            System.out.println("⚠️ Found delivery records but none are DELIVERED");

            // ✅ Log all delivery statuses for debugging
            for (Delivery delivery : deliveries) {
                System.out.println("  - Delivery " + delivery.getDeliveryId() + ": " + delivery.getStatus());
            }
        } else {
            System.out.println("⚠️ No delivery records found for transaction " + transaction.getTransactionId());
        }

        // ✅ Fallback: Check transaction status for non-delivery transactions or old data
        String transactionStatus = transaction.getStatus().toString().toLowerCase();
        boolean transactionCompleted = transactionStatus.equals("completed") || transactionStatus.equals("delivered");

        System.out.println("Transaction status check - eligible: " + transactionCompleted + " (status: " + transactionStatus + ")");

        if (transactionCompleted) {
            System.out.println("✅ Transaction eligible for review - transaction status completed");
            System.out.println("=== END REVIEW ELIGIBILITY CHECK ===");
            return true;
        }

        System.out.println("❌ Transaction NOT eligible for review");
        System.out.println("  - No DELIVERED deliveries found");
        System.out.println("  - Transaction status is not completed/delivered: " + transactionStatus);
        System.out.println("=== END REVIEW ELIGIBILITY CHECK ===");
        return false;
    }

    /**
     * Get all reviews for a specific transaction
     */
    public List<Review> getTransactionReviews(Long transactionId) {
        System.out.println("Fetching reviews for transaction: " + transactionId);
        return reviewRepository.findByTransactionId(transactionId);
    }

    /**
     * Get all reviews for a specific book
     */
    public List<Review> getBookReviews(Long bookId) {
        System.out.println("Fetching reviews for book: " + bookId);
        return reviewRepository.findByBookId(bookId);
    }

    /**
     * Get all reviews written by a specific user
     */
    public List<Review> getUserReviews(Long userId) {
        System.out.println("Fetching reviews written by user: " + userId);
        return reviewRepository.findByReviewerUserId(userId);
    }

    /**
     * Get all reviews received by a specific user
     */
    public List<Review> getReviewsForUser(Long userId) {
        System.out.println("Fetching reviews received by user: " + userId);
        return reviewRepository.findByReviewedUserId(userId);
    }

    /**
     * Get average seller rating for a user
     */
    public Double getUserAverageRating(Long userId) {
        System.out.println("Calculating average rating for user: " + userId);
        Double average = reviewRepository.getAverageSellerRating(userId);
        return average != null ? average : 0.0;
    }

    /**
     * Get average book rating
     */
    public Double getBookAverageRating(Long bookId) {
        System.out.println("Calculating average rating for book: " + bookId);
        Double average = reviewRepository.getAverageBookRating(bookId);
        return average != null ? average : 0.0;
    }

    /**
     * ✅ UPDATED: Check if a user can review a specific transaction (includes delivery check)
     */
    public boolean canUserReviewTransaction(Long userId, Long transactionId) {
        try {
            Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);
            if (!transactionOpt.isPresent()) {
                return false;
            }

            Transaction transaction = transactionOpt.get();

            // Check if user is involved
            if (!isUserInvolvedInTransaction(userId, transaction)) {
                return false;
            }

            // ✅ Check if transaction/delivery is eligible
            if (!isTransactionEligibleForReview(transaction)) {
                return false;
            }

            // Check if THIS USER already reviewed THIS TRANSACTION
            Optional<Review> existingReview = reviewRepository.findByTransactionIdAndReviewerUserId(transactionId, userId);
            return !existingReview.isPresent();

        } catch (Exception e) {
            System.err.println("Error checking if user can review transaction: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if user is involved in the transaction
     */
    private boolean isUserInvolvedInTransaction(Long userId, Transaction transaction) {
        boolean isInvolved = userId.equals(transaction.getUserId()) ||
                userId.equals(transaction.getBorrowerId()) ||
                userId.equals(transaction.getSellerId()) ||
                userId.equals(transaction.getLenderId()) ||
                userId.equals(transaction.getExchangerId());

        System.out.println("User " + userId + " involved in transaction: " + isInvolved);
        return isInvolved;
    }

    /**
     * Determine who is being reviewed based on transaction type
     */
    private Long determineReviewedUser(Transaction transaction, Long reviewerUserId) {
        System.out.println("Determining reviewed user for transaction type: " + transaction.getType());

        try {
            switch (transaction.getType()) {
                case SALE:
                    return reviewerUserId.equals(transaction.getBorrowerId()) ?
                            transaction.getSellerId() : transaction.getBorrowerId();

                case LEND:
                    return reviewerUserId.equals(transaction.getBorrowerId()) ?
                            transaction.getLenderId() : transaction.getBorrowerId();

                case EXCHANGE:
                    return reviewerUserId.equals(transaction.getBorrowerId()) ?
                            transaction.getExchangerId() : transaction.getBorrowerId();

                case BIDDING:
                case AUCTION:
                    return reviewerUserId.equals(transaction.getBorrowerId()) ?
                            transaction.getSellerId() : transaction.getBorrowerId();

                default:
                    System.err.println("Unknown transaction type: " + transaction.getType());
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Error determining reviewed user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get review statistics for a user
     */
    public ReviewStats getUserReviewStats(Long userId) {
        List<Review> reviewsWritten = getUserReviews(userId);
        List<Review> reviewsReceived = getReviewsForUser(userId);
        Double averageRating = getUserAverageRating(userId);

        return new ReviewStats(
                reviewsWritten.size(),
                reviewsReceived.size(),
                averageRating
        );
    }

    /**
     * Inner class for review statistics
     */
    public static class ReviewStats {
        private final int reviewsWritten;
        private final int reviewsReceived;
        private final double averageRating;

        public ReviewStats(int reviewsWritten, int reviewsReceived, double averageRating) {
            this.reviewsWritten = reviewsWritten;
            this.reviewsReceived = reviewsReceived;
            this.averageRating = averageRating;
        }

        public int getReviewsWritten() { return reviewsWritten; }
        public int getReviewsReceived() { return reviewsReceived; }
        public double getAverageRating() { return averageRating; }
    }
}