package controller.Delivery;

import model.dto.Delivery.TransactionDto.*;
import service.Delivery.impl.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionCreateDto createDto) {
        try {
            TransactionResponseDto transaction = transactionService.createTransaction(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions() {
        try {
            List<TransactionResponseDto> transactions = transactionService.getAllTransactions();
            log.info("Retrieved {} transactions", transactions.size());
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Error retrieving all transactions: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long transactionId) {
        try {
            return transactionService.getTransactionById(transactionId)
                    .map(transaction -> ResponseEntity.ok(transaction))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error retrieving transaction {}: {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving transaction"));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTransactionsByStatus(@PathVariable String status) {
        try {
            List<TransactionResponseDto> transactions = transactionService.getTransactionsByStatus(status);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid status: " + status));
        } catch (Exception e) {
            log.error("Error retrieving transactions by status {}: {}", status, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving transactions"));
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getTransactionsByType(@PathVariable String type) {
        try {
            List<TransactionResponseDto> transactions = transactionService.getTransactionsByType(type);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid type: " + type));
        } catch (Exception e) {
            log.error("Error retrieving transactions by type {}: {}", type, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving transactions"));
        }
    }

    @GetMapping("/payment-status/{paymentStatus}")
    public ResponseEntity<?> getTransactionsByPaymentStatus(@PathVariable String paymentStatus) {
        try {
            List<TransactionResponseDto> transactions = transactionService.getTransactionsByPaymentStatus(paymentStatus);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid payment status: " + paymentStatus));
        } catch (Exception e) {
            log.error("Error retrieving transactions by payment status {}: {}", paymentStatus, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving transactions"));
        }
    }

    @GetMapping("/hub/{hubId}/revenue")
    public ResponseEntity<?> getHubRevenue(@PathVariable Long hubId) {
        try {
            HubRevenueDto revenue = transactionService.getHubRevenue(hubId);
            return ResponseEntity.ok(revenue);
        } catch (Exception e) {
            log.error("Error retrieving hub revenue for hub {}: {}", hubId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving hub revenue"));
        }
    }

    @GetMapping("/revenue-summary")
    public ResponseEntity<?> getRevenueSummary() {
        try {
            RevenueSummaryDto summary = transactionService.getRevenueSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            log.error("Error retrieving revenue summary: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving revenue summary"));
        }
    }

    @GetMapping("/hub/{hubId}/transactions")
    public ResponseEntity<?> getTransactionsByHub(@PathVariable Long hubId) {
        try {
            List<TransactionResponseDto> transactions = transactionService.getTransactionsByHub(hubId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Error retrieving transactions for hub {}: {}", hubId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving hub transactions"));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getTransactionStats() {
        try {
            List<TransactionStatsDto> stats = transactionService.getTransactionStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error retrieving transaction stats: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving transaction stats"));
        }
    }

    @PutMapping("/{transactionId}/status")
    public ResponseEntity<?> updateTransactionStatus(
            @PathVariable Long transactionId,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            String status = statusUpdate.get("status");
            TransactionResponseDto updatedTransaction = transactionService.updateTransactionStatus(transactionId, status);
            return ResponseEntity.ok(updatedTransaction);
        } catch (Exception e) {
            log.error("Error updating transaction status for {}: {}", transactionId, e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{transactionId}/payment-status")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable Long transactionId,
            @RequestBody Map<String, String> paymentStatusUpdate) {
        try {
            String paymentStatus = paymentStatusUpdate.get("paymentStatus");
            TransactionResponseDto updatedTransaction = transactionService.updatePaymentStatus(transactionId, paymentStatus);
            return ResponseEntity.ok(updatedTransaction);
        } catch (Exception e) {
            log.error("Error updating payment status for {}: {}", transactionId, e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long transactionId) {
        try {
            transactionService.deleteTransaction(transactionId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting transaction {}: {}", transactionId, e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // Helper method to create consistent error responses
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", true);
        error.put("message", message);
        error.put("timestamp", System.currentTimeMillis());
        return error;
    }
}