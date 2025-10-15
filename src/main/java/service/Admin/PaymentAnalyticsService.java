package service.Admin;

import model.dto.Admin.PaymentAnalyticsDTO;
import model.entity.Transaction;
import model.repo.Admin.PaymentAnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentAnalyticsService {

    @Autowired
    private PaymentAnalyticsRepository transactionRepository;

    public Page<PaymentAnalyticsDTO> getFilteredTransactions(
            String searchTerm,
            Transaction.TransactionStatus status,
            Transaction.TransactionType type,
            Transaction.PaymentStatus paymentStatus,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size) {

        // Create pageable without sort to avoid the sorting issue
        Pageable pageable = PageRequest.of(page, size);

        // Convert enums to strings for native query, use empty string instead of null
        String statusStr = status != null ? status.name() : "";
        String typeStr = type != null ? type.name() : "";
        String paymentStatusStr = paymentStatus != null ? paymentStatus.name() : "";

        Page<Transaction> transactionPage = transactionRepository.findTransactionsWithFiltersNative(
                searchTerm, statusStr, typeStr, paymentStatusStr, startDate, endDate, pageable
        );

        return transactionPage.map(this::convertToDTO);
    }

    public List<PaymentAnalyticsDTO> getAllFilteredTransactions(
            String searchTerm,
            Transaction.TransactionStatus status,
            Transaction.TransactionType type,
            Transaction.PaymentStatus paymentStatus,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        // Convert enums to strings for native query, use empty string instead of null
        String statusStr = status != null ? status.name() : "";
        String typeStr = type != null ? type.name() : "";
        String paymentStatusStr = paymentStatus != null ? paymentStatus.name() : "";

        List<Transaction> transactions = transactionRepository.findAllTransactionsWithFiltersNative(
                searchTerm, statusStr, typeStr, paymentStatusStr, startDate, endDate
        );

        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalRevenue(
            String searchTerm,
            Transaction.TransactionStatus status,
            Transaction.TransactionType type,
            Transaction.PaymentStatus paymentStatus,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        String statusStr = status != null ? status.name() : "";
        String typeStr = type != null ? type.name() : "";
        String paymentStatusStr = paymentStatus != null ? paymentStatus.name() : "";

        List<Transaction> transactions = transactionRepository.findAllTransactionsWithFiltersNative(
                searchTerm, statusStr, typeStr, paymentStatusStr, startDate, endDate
        );

        return transactions.stream()
                .filter(t -> t.getPaymentStatus() == Transaction.PaymentStatus.COMPLETED)
                .map(Transaction::getPaymentAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getTotalTransactionCount(
            String searchTerm,
            Transaction.TransactionStatus status,
            Transaction.TransactionType type,
            Transaction.PaymentStatus paymentStatus,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        String statusStr = status != null ? status.name() : "";
        String typeStr = type != null ? type.name() : "";
        String paymentStatusStr = paymentStatus != null ? paymentStatus.name() : "";

        List<Transaction> transactions = transactionRepository.findAllTransactionsWithFiltersNative(
                searchTerm, statusStr, typeStr, paymentStatusStr, startDate, endDate
        );

        return (long) transactions.size();
    }

    public Long getPendingPaymentsCount(
            String searchTerm,
            Transaction.TransactionStatus status,
            Transaction.TransactionType type,
            Transaction.PaymentStatus paymentStatus,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        String statusStr = status != null ? status.name() : "";
        String typeStr = type != null ? type.name() : "";
        String paymentStatusStr = paymentStatus != null ? paymentStatus.name() : "";

        List<Transaction> transactions = transactionRepository.findAllTransactionsWithFiltersNative(
                searchTerm, statusStr, typeStr, paymentStatusStr, startDate, endDate
        );

        return transactions.stream()
                .filter(t -> t.getPaymentStatus() == Transaction.PaymentStatus.PENDING)
                .count();
    }

    public BigDecimal getRefundedAmount(
            String searchTerm,
            Transaction.TransactionStatus status,
            Transaction.TransactionType type,
            Transaction.PaymentStatus paymentStatus,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        String statusStr = status != null ? status.name() : "";
        String typeStr = type != null ? type.name() : "";
        String paymentStatusStr = paymentStatus != null ? paymentStatus.name() : "";

        List<Transaction> transactions = transactionRepository.findAllTransactionsWithFiltersNative(
                searchTerm, statusStr, typeStr, paymentStatusStr, startDate, endDate
        );

        return transactions.stream()
                .filter(t -> t.getPaymentStatus() == Transaction.PaymentStatus.REFUNDED)
                .map(Transaction::getPaymentAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public PaymentAnalyticsDTO getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + transactionId));
        return convertToDTO(transaction);
    }

    private PaymentAnalyticsDTO convertToDTO(Transaction transaction) {
        return new PaymentAnalyticsDTO(
                transaction.getTransactionId(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getPaymentStatus(),
                transaction.getPaymentAmount() != null ? transaction.getPaymentAmount() : BigDecimal.ZERO,
                transaction.getCreatedAt(),
                transaction.getStartDate(),
                transaction.getEndDate(),
                transaction.getBookId(),
                transaction.getUserId()
        );
    }
}