package model.entity.Bid;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_bid")
@Data
public class User_Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placemet_id")
    private int placemet_id;

    @Column(name = "bid_id")
    private int bid_id;

    @Column(name="book_id")
    private Long bookId;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "bid_amount")
    private double bid_amount;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    // Automatically set current time before saving
    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
    }

    public User_Bid(int bid_id, Long bookId, int user_id, double bid_amount, String name) {
        this.bid_id = bid_id;
        this.bookId = bookId;
        this.user_id = user_id;
        this.bid_amount = bid_amount;
        this.name = name;
    }

    public User_Bid() {
    }

    public int getPlacemet_id() {
        return placemet_id;
    }

    public void setPlacemet_id(int placemet_id) {
        this.placemet_id = placemet_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getBid_amount() {
        return bid_amount;
    }

    public void setBid_amount(double bid_amount) {
        this.bid_amount = bid_amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
