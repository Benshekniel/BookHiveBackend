package controller.Admin;

import model.dto.Admin.PaymentAnalyticsDTO;
import model.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.Admin.PaymentAnalyticsService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:9999")
@RequestMapping("/api/admin/payment-analytics")
public class PaymentAnalyticsController {

    @Autowired
    private PaymentAnalyticsService paymentAnalyticsService;

    @GetMapping("/transactions")
    public ResponseEntity<Page<PaymentAnalyticsDTO>> getFilteredTransactions(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Transaction.TransactionStatus status,
            @RequestParam(required = false) Transaction.TransactionType type,
            @RequestParam(required = false) Transaction.PaymentStatus paymentStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PaymentAnalyticsDTO> result = paymentAnalyticsService.getFilteredTransactions(
                searchTerm, status, type, paymentStatus, startDate, endDate, page, size
        );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/stats")
    public ResponseEntity<PaymentAnalyticsDTO.PaymentStatsDTO> getPaymentStats(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Transaction.TransactionStatus status,
            @RequestParam(required = false) Transaction.TransactionType type,
            @RequestParam(required = false) Transaction.PaymentStatus paymentStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        BigDecimal totalRevenue = paymentAnalyticsService.getTotalRevenue(
                searchTerm, status, type, paymentStatus, startDate, endDate
        );

        Long totalTransactions = paymentAnalyticsService.getTotalTransactionCount(
                searchTerm, status, type, paymentStatus, startDate, endDate
        );

        Long pendingPayments = paymentAnalyticsService.getPendingPaymentsCount(
                searchTerm, status, type, paymentStatus, startDate, endDate
        );

        BigDecimal refundedAmount = paymentAnalyticsService.getRefundedAmount(
                searchTerm, status, type, paymentStatus, startDate, endDate
        );

        PaymentAnalyticsDTO.PaymentStatsDTO stats = new PaymentAnalyticsDTO.PaymentStatsDTO(
                totalRevenue, totalTransactions, pendingPayments, refundedAmount
        );

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportTransactions(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Transaction.TransactionStatus status,
            @RequestParam(required = false) Transaction.TransactionType type,
            @RequestParam(required = false) Transaction.PaymentStatus paymentStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<PaymentAnalyticsDTO> transactions = paymentAnalyticsService.getAllFilteredTransactions(
                searchTerm, status, type, paymentStatus, startDate, endDate
        );

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Transaction ID,Type,Status,Payment Status,Amount (LKR),Created At,Book ID,User ID\n");

        for (PaymentAnalyticsDTO transaction : transactions) {
            csvBuilder.append(String.format("%d,%s,%s,%s,%.2f,%s,%d,%d\n",
                    transaction.getTransactionId(),
                    transaction.getType() != null ? transaction.getType().toString() : "",
                    transaction.getStatus() != null ? transaction.getStatus().toString() : "",
                    transaction.getPaymentStatus() != null ? transaction.getPaymentStatus().toString() : "",
                    transaction.getPaymentAmount() != null ? transaction.getPaymentAmount() : BigDecimal.ZERO,
                    transaction.getCreatedAt() != null ? transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "",
                    transaction.getBookId() != null ? transaction.getBookId() : 0,
                    transaction.getUserId() != null ? transaction.getUserId() : 0
            ));
        }

        String filename = "bookhive-payment-analytics-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".csv";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(csvBuilder.toString().getBytes(), headers, HttpStatus.OK);
    }

    @GetMapping("/transaction-types")
    public ResponseEntity<Transaction.TransactionType[]> getTransactionTypes() {
        return ResponseEntity.ok(Transaction.TransactionType.values());
    }

    @GetMapping("/transaction-statuses")
    public ResponseEntity<Transaction.TransactionStatus[]> getTransactionStatuses() {
        return ResponseEntity.ok(Transaction.TransactionStatus.values());
    }

    @GetMapping("/payment-statuses")
    public ResponseEntity<Transaction.PaymentStatus[]> getPaymentStatuses() {
        return ResponseEntity.ok(Transaction.PaymentStatus.values());
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<PaymentAnalyticsDTO> getTransactionById(@PathVariable Long id) {
        try {
            PaymentAnalyticsDTO transaction = paymentAnalyticsService.getTransactionById(id);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}