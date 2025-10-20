package model.entity.Bid;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "exchangebooks")
@Data
public class Exchange_Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchangeId")
    private Integer exchangeId;

    @Column(name="userId")
    private int userId;

    @Column(name="approverId")
    private int approverId;


    @Column(name="userBookId")
    private Long userBookId;

    @Column(name="approverBookId")
    private Long approverBookId;


    @Column(name = "userDeliveryPrice")
    private double userDeliveryPrice;

    @Column(name = "userHandlingPrice")
    private double userHandlingPrice;

    @Column(name = "approverDeliveryPrice")
    private double approverDeliveryPrice;

    @Column(name = "approverHandlingPrice")
    private double approverHandlingPrice;

    @Column(name = "status") //APPROVED REJECTED PENDING
    private String status;

    @Column(name = "rejectReason")
    private String rejectReason;

    public Exchange_Books(Long approverBookId, Long userBookId, int userId, int approverId, double userDeliveryPrice, double userHandlingPrice, String status) {
        this.approverBookId = approverBookId;
        this.userBookId = userBookId;
        this.userId = userId;
        this.approverId = approverId;
        this.userDeliveryPrice = userDeliveryPrice;
        this.userHandlingPrice = userHandlingPrice;
        this.status = status;
    }

    public Exchange_Books() {
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getApproverBookId() {
        return approverBookId;
    }

    public void setApproverBookId(Long approverBookId) {
        this.approverBookId = approverBookId;
    }

    public Long getUserBookId() {
        return userBookId;
    }

    public void setUserBookId(Long userBookId) {
        this.userBookId = userBookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getApproverId() {
        return approverId;
    }

    public void setApproverId(int approverId) {
        this.approverId = approverId;
    }

    public double getUserDeliveryPrice() {
        return userDeliveryPrice;
    }

    public void setUserDeliveryPrice(double userDeliveryPrice) {
        this.userDeliveryPrice = userDeliveryPrice;
    }

    public double getUserHandlingPrice() {
        return userHandlingPrice;
    }

    public void setUserHandlingPrice(double userHandlingPrice) {
        this.userHandlingPrice = userHandlingPrice;
    }

    public double getApproverDeliveryPrice() {
        return approverDeliveryPrice;
    }

    public void setApproverDeliveryPrice(double approverDeliveryPrice) {
        this.approverDeliveryPrice = approverDeliveryPrice;
    }

    public double getApproverHandlingPrice() {
        return approverHandlingPrice;
    }

    public void setApproverHandlingPrice(double approverHandlingPrice) {
        this.approverHandlingPrice = approverHandlingPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
