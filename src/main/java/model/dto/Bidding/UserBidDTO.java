package model.dto.Bidding;

public class UserBidDTO {

    private int bidId;
    private int userId;
    private Long bookId;
    private double bidAmount;
    private String name;

    public UserBidDTO(int bidId, int userId, Long bookId, double bidAmount, String name) {
        this.bidId = bidId;
        this.userId = userId;
        this.bookId = bookId;
        this.bidAmount = bidAmount;
        this.name = name;
    }

    public UserBidDTO() {
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
