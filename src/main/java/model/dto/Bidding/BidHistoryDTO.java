package model.dto.Bidding;

import java.time.LocalDateTime;

public class BidHistoryDTO {

    private int bidId;
    private Long bookId;
    private LocalDateTime biddingStartDate;
    private LocalDateTime biddingEndDate;
    private Double initialBidAmount;
    private boolean bidEnd;
    private String bidWinner;

    public BidHistoryDTO(int bidId, Long bookId, LocalDateTime biddingStartDate, LocalDateTime biddingEndDate, Double initialBidAmount, boolean bidEnd, String bidWinner) {
        this.bidId = bidId;
        this.bookId = bookId;
        this.biddingStartDate = biddingStartDate;
        this.biddingEndDate = biddingEndDate;
        this.initialBidAmount = initialBidAmount;
        this.bidEnd = bidEnd;
        this.bidWinner = bidWinner;
    }

    public BidHistoryDTO() {
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getBiddingStartDate() {
        return biddingStartDate;
    }

    public void setBiddingStartDate(LocalDateTime biddingStartDate) {
        this.biddingStartDate = biddingStartDate;
    }

    public LocalDateTime getBiddingEndDate() {
        return biddingEndDate;
    }

    public void setBiddingEndDate(LocalDateTime biddingEndDate) {
        this.biddingEndDate = biddingEndDate;
    }

    public Double getInitialBidAmount() {
        return initialBidAmount;
    }

    public void setInitialBidAmount(Double initialBidAmount) {
        this.initialBidAmount = initialBidAmount;
    }

    public boolean isBidEnd() {
        return bidEnd;
    }

    public void setBidEnd(boolean bidEnd) {
        this.bidEnd = bidEnd;
    }

    public String getBidWinner() {
        return bidWinner;
    }

    public void setBidWinner(String bidWinner) {
        this.bidWinner = bidWinner;
    }
}
