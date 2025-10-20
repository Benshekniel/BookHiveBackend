package controller.User;

import lombok.RequiredArgsConstructor;
import model.dto.User.ReviewSubmissionDto;
import model.entity.Review;
import service.User.impl.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:9999")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> submitReview(@RequestBody ReviewSubmissionDto reviewDto) {
        try {
            System.out.println("=== CONTROLLER DEBUG ===");
            System.out.println("Received review DTO: " + reviewDto);
            System.out.println("========================");

            // ✅ Use the validation methods from the DTO
            if (!reviewDto.isValid()) {
                String errorMessage = reviewDto.getValidationError();
                System.err.println("Validation error: " + errorMessage);
                return ResponseEntity.badRequest().body(errorMessage);
            }

            Review review = reviewService.submitReview(reviewDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(review);

        } catch (RuntimeException e) {
            System.err.println("Runtime error submitting review: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error submitting review: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<Review>> getTransactionReviews(@PathVariable Long transactionId) {
        try {
            List<Review> reviews = reviewService.getTransactionReviews(transactionId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Review>> getBookReviews(@PathVariable Long bookId) {
        try {
            List<Review> reviews = reviewService.getBookReviews(bookId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getUserReviews(@PathVariable Long userId) {
        try {
            List<Review> reviews = reviewService.getUserReviews(userId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}/average-rating")
    public ResponseEntity<Double> getUserAverageRating(@PathVariable Long userId) {
        try {
            Double averageRating = reviewService.getUserAverageRating(userId);
            return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/book/{bookId}/average-rating")
    public ResponseEntity<Double> getBookAverageRating(@PathVariable Long bookId) {
        try {
            Double averageRating = reviewService.getBookAverageRating(bookId);
            return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Check if user can review a transaction
    @GetMapping("/can-review/{transactionId}/{userId}")
    public ResponseEntity<Boolean> canUserReviewTransaction(@PathVariable Long transactionId, @PathVariable Long userId) {
        try {
            boolean canReview = reviewService.canUserReviewTransaction(userId, transactionId);
            return ResponseEntity.ok(canReview);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}