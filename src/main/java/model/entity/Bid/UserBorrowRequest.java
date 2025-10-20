package model.entity.Bid;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserBorrowRequest")
@Data
public class UserBorrowRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;


    @Column(name = "name")
    private String name;

    @Column(name = "userId")
    private int userId;

    @Column(name = "ownerEmail")
    private String ownerEmail;

//=====================BACKEND
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "bookName")
    private String bookName;

    @Column(name = "bookImageName")
    private String bookImageName;

    @Column(name = "author")
    private String author;

    @Column(name = "lendPrice")
    private double lendPrice;


    @Column(name = "ownerName")
    private String ownerName;


    @Column(name = "ownerId") //get from alluser by using email from bookId
    private int ownerId;


    @Column(name = "status") //APPROVED REJECTED PENDING COMPLETED
    private String status;

//=====================BACKEND



    @Column(name = "deliveryPrice")
    private double deliveryPrice;

    @Column(name = "handlingPrice")
    private double handlingPrice;

    // Timestamps
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public UserBorrowRequest(String name, int userId, String ownerEmail, Long bookId, String bookName, String bookImageName, String author, double lendPrice, String ownerName, int ownerId, String status, double deliveryPrice, double handlingPrice) {
        this.name = name;
        this.userId = userId;
        this.ownerEmail = ownerEmail;
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookImageName = bookImageName;
        this.author = author;
        this.lendPrice = lendPrice;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        this.status = status;
        this.deliveryPrice = deliveryPrice;
        this.handlingPrice = handlingPrice;
    }

    public UserBorrowRequest() {
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookImageName() {
        return bookImageName;
    }

    public void setBookImageName(String bookImageName) {
        this.bookImageName = bookImageName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getLendPrice() {
        return lendPrice;
    }

    public void setLendPrice(double lendPrice) {
        this.lendPrice = lendPrice;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public double getHandlingPrice() {
        return handlingPrice;
    }

    public void setHandlingPrice(double handlingPrice) {
        this.handlingPrice = handlingPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}


