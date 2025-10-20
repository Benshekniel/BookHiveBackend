package model.dto;

import model.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NewTransactionDTO {

//    private Transaction.TransactionType type;
//
//    private Transaction.TransactionStatus status;
//
//    private BigDecimal paymentAmount;
//
//    private Transaction.PaymentStatus paymentStatus;
//
//    private Long bookId;
//
//    private Long userId;
private Transaction.TransactionType type;

    private Transaction.TransactionStatus status;

    private BigDecimal paymentAmount;
    private String paMethodNew;

    private Transaction.PaymentStatus paymentStatus;

    private Long bookId;

    private Long userId;

    public NewTransactionDTO() {
    }

    public NewTransactionDTO(Transaction.TransactionType type, Transaction.TransactionStatus status, BigDecimal paymentAmount, String paMethodNew, Transaction.PaymentStatus paymentStatus, Long bookId, Long userId) {
        this.type = type;
        this.status = status;
        this.paymentAmount = paymentAmount;
        this.paMethodNew = paMethodNew;
        this.paymentStatus = paymentStatus;
        this.bookId = bookId;
        this.userId = userId;
    }

    public Transaction.TransactionType getType() {
        return type;
    }

    public void setType(Transaction.TransactionType type) {
        this.type = type;
    }

    public Transaction.TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(Transaction.TransactionStatus status) {
        this.status = status;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaMethodNew() {
        return paMethodNew;
    }

    public void setPaMethodNew(String paMethodNew) {
        this.paMethodNew = paMethodNew;
    }

    public Transaction.PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Transaction.PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
