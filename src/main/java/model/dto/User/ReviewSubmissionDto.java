package model.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for review submission
 */
public class ReviewSubmissionDto {

    @JsonProperty("transactionId")
    private Long transactionId;

    @JsonProperty("reviewerUserId")
    private Long reviewerUserId;

    @JsonProperty("bookId")
    private Long bookId;

    @JsonProperty("reviewedUserId")
    private Long reviewedUserId;

    @JsonProperty("sellerRating")
    private Integer sellerRating;

    @JsonProperty("bookConditionRating")
    private Integer bookConditionRating;

    @JsonProperty("overallRating")
    private Integer overallRating;

    // ✅ Frontend sends "comment" but entity expects "reviewComment"
    @JsonProperty("comment")
    private String comment;

    // ✅ Default constructor for Jackson
    public ReviewSubmissionDto() {}

    // ✅ Constructor with all parameters
    public ReviewSubmissionDto(Long transactionId, Long reviewerUserId, Long bookId, Long reviewedUserId,
                               Integer sellerRating, Integer bookConditionRating,
                               Integer overallRating, String comment) {
        this.transactionId = transactionId;
        this.reviewerUserId = reviewerUserId;
        this.bookId = bookId;
        this.reviewedUserId = reviewedUserId;
        this.sellerRating = sellerRating;
        this.bookConditionRating = bookConditionRating;
        this.overallRating = overallRating;
        this.comment = comment;
    }

    // ✅ Getters and Setters
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getReviewerUserId() {
        return reviewerUserId;
    }

    public void setReviewerUserId(Long reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getReviewedUserId() {
        return reviewedUserId;
    }

    public void setReviewedUserId(Long reviewedUserId) {
        this.reviewedUserId = reviewedUserId;
    }

    public Integer getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(Integer sellerRating) {
        this.sellerRating = sellerRating;
    }

    public Integer getBookConditionRating() {
        return bookConditionRating;
    }

    public void setBookConditionRating(Integer bookConditionRating) {
        this.bookConditionRating = bookConditionRating;
    }

    public Integer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Integer overallRating) {
        this.overallRating = overallRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // ✅ Helper method to get review comment for entity mapping
    public String getReviewComment() {
        return comment;
    }

    // ✅ Validation helper methods
    public boolean isValid() {
        return transactionId != null
                && reviewerUserId != null
                && bookId != null
                && sellerRating != null && sellerRating >= 1 && sellerRating <= 5
                && bookConditionRating != null && bookConditionRating >= 1 && bookConditionRating <= 5
                && overallRating != null && overallRating >= 1 && overallRating <= 5
                && comment != null && !comment.trim().isEmpty();
    }

    public String getValidationError() {
        if (transactionId == null) return "Transaction ID is required";
        if (reviewerUserId == null) return "Reviewer User ID is required";
        if (bookId == null) return "Book ID is required";
        if (sellerRating == null || sellerRating < 1 || sellerRating > 5)
            return "Seller rating must be between 1 and 5";
        if (bookConditionRating == null || bookConditionRating < 1 || bookConditionRating > 5)
            return "Book condition rating must be between 1 and 5";
        if (overallRating == null || overallRating < 1 || overallRating > 5)
            return "Overall rating must be between 1 and 5";
        if (comment == null || comment.trim().isEmpty())
            return "Comment is required";
        if (comment.trim().length() < 10)
            return "Comment must be at least 10 characters long";
        if (comment.trim().length() > 500)
            return "Comment must not exceed 500 characters";
        return null;
    }

    @Override
    public String toString() {
        return "ReviewSubmissionDto{" +
                "transactionId=" + transactionId +
                ", reviewerUserId=" + reviewerUserId +
                ", bookId=" + bookId +
                ", reviewedUserId=" + reviewedUserId +
                ", sellerRating=" + sellerRating +
                ", bookConditionRating=" + bookConditionRating +
                ", overallRating=" + overallRating +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReviewSubmissionDto that = (ReviewSubmissionDto) o;

        if (transactionId != null ? !transactionId.equals(that.transactionId) : that.transactionId != null)
            return false;
        if (reviewerUserId != null ? !reviewerUserId.equals(that.reviewerUserId) : that.reviewerUserId != null)
            return false;
        if (bookId != null ? !bookId.equals(that.bookId) : that.bookId != null)
            return false;
        if (reviewedUserId != null ? !reviewedUserId.equals(that.reviewedUserId) : that.reviewedUserId != null)
            return false;
        if (sellerRating != null ? !sellerRating.equals(that.sellerRating) : that.sellerRating != null)
            return false;
        if (bookConditionRating != null ? !bookConditionRating.equals(that.bookConditionRating) : that.bookConditionRating != null)
            return false;
        if (overallRating != null ? !overallRating.equals(that.overallRating) : that.overallRating != null)
            return false;
        return comment != null ? comment.equals(that.comment) : that.comment == null;
    }

    @Override
    public int hashCode() {
        int result = transactionId != null ? transactionId.hashCode() : 0;
        result = 31 * result + (reviewerUserId != null ? reviewerUserId.hashCode() : 0);
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        result = 31 * result + (reviewedUserId != null ? reviewedUserId.hashCode() : 0);
        result = 31 * result + (sellerRating != null ? sellerRating.hashCode() : 0);
        result = 31 * result + (bookConditionRating != null ? bookConditionRating.hashCode() : 0);
        result = 31 * result + (overallRating != null ? overallRating.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}