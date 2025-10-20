package model.entity.Bid;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bid_history")
@Data
public class Bid_History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private int bid_id;

    @Column(name="book_id")
    private Long bookId;

    @Column(name = "bidding_start_date")
    private LocalDateTime biddingStartDate;

    @Column(name = "bidding_end_date")
    private LocalDateTime biddingEndDate;

    @Column(name = "initial_bid_amount")
    private Double initial_bid_amount;

    @Column(name = "bidEnd",nullable = true)
    private boolean bidEnd;

    @Column(name = "bidWinner",nullable = true)
    private String bidWinner;


    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    @Column(name="bookImage", length = 255)
    private String bookImage;

    public Bid_History(int bid_id, Long bookId, LocalDateTime biddingStartDate, LocalDateTime biddingEndDate, Double initial_bid_amount, boolean bidEnd, String bidWinner, String bookImage) {
        this.bid_id = bid_id;
        this.bookId = bookId;
        this.biddingStartDate = biddingStartDate;
        this.biddingEndDate = biddingEndDate;
        this.initial_bid_amount = initial_bid_amount;
        this.bidEnd = bidEnd;
        this.bidWinner = bidWinner;
        this.bookImage = bookImage;
    }

    public Bid_History() {
    }

    public int getBid_id() {
        return bid_id;
    }

    public void setBid_id(int bid_id) {
        this.bid_id = bid_id;
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

    public Double getInitial_bid_amount() {
        return initial_bid_amount;
    }

    public void setInitial_bid_amount(Double initial_bid_amount) {
        this.initial_bid_amount = initial_bid_amount;
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
