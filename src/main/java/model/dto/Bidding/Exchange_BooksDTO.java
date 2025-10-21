package model.dto.Bidding;

public class Exchange_BooksDTO {


    private int userId;
    private int approverId;

    private Long userBookId;
    private Long approverBookId;

    private double userDeliveryPrice;
    private double userHandlingPrice;


    private String  approverEmail;

    public Exchange_BooksDTO(int userId, Long userBookId, Long approverBookId, double userDeliveryPrice, double userHandlingPrice, String approverEmail) {
        this.userId = userId;
        this.userBookId = userBookId;
        this.approverBookId = approverBookId;
        this.userDeliveryPrice = userDeliveryPrice;
        this.userHandlingPrice = userHandlingPrice;
        this.approverEmail = approverEmail;
    }

    public Exchange_BooksDTO() {
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

    public Long getUserBookId() {
        return userBookId;
    }

    public void setUserBookId(Long userBookId) {
        this.userBookId = userBookId;
    }

    public Long getApproverBookId() {
        return approverBookId;
    }

    public void setApproverBookId(Long approverBookId) {
        this.approverBookId = approverBookId;
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

    public String getApproverEmail() {
        return approverEmail;
    }

    public void setApproverEmail(String approverEmail) {
        this.approverEmail = approverEmail;
    }
}
