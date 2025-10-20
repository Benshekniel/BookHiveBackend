package model.dto.Bidding;

import jakarta.persistence.Column;

public class UserBorrowRequestDTO {

    private int userId;
    private String name;

    private Long bookId;
    private String ownerEmail;

    private double deliveryPrice;
    private double handlingPrice;


    private String bookName;
    private String bookImageName;
    private String author;
    private double lendPrice;
    private int ownerId;
    private String ownerName;
    private String status;  //APPROVED REJECTED PENDING COMPLETED


    public UserBorrowRequestDTO(int userId, String name, Long bookId, String ownerEmail, double deliveryPrice, double handlingPrice) {
        this.userId = userId;
        this.name = name;
        this.bookId = bookId;
        this.ownerEmail = ownerEmail;
        this.deliveryPrice = deliveryPrice;
        this.handlingPrice = handlingPrice;
    }

    public UserBorrowRequestDTO() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
