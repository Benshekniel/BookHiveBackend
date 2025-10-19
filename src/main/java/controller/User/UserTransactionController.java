package controller.User;

import model.dto.UserTransactionDto;
import service.User.impl.UserTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user-transactions")
public class UserTransactionController {

    @Autowired
    private UserTransactionService transactionService;

    // Get user's transactions with filtering and pagination
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<UserTransactionDto.TransactionResponseDto>> getUserTransactions(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        try {
            UserTransactionDto.TransactionFilterDto filter = new UserTransactionDto.TransactionFilterDto();
            filter.setStatus(status);
            filter.setType(type);
            filter.setPaymentStatus(paymentStatus);
            filter.setFromDate(fromDate);
            filter.setToDate(toDate);
            filter.setSortBy(sortBy);
            filter.setSortDirection(sortDirection);
            filter.setPage(page);
            filter.setSize(size);

            Page<UserTransactionDto.TransactionResponseDto> transactions =
                    transactionService.getUserTransactions(userId, filter);

            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get specific transaction by ID
    @GetMapping("/{transactionId}")
    public ResponseEntity<UserTransactionDto.TransactionResponseDto> getTransactionById(
            @PathVariable Long transactionId) {
        try {
            UserTransactionDto.TransactionResponseDto transaction =
                    transactionService.getTransactionById(transactionId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get transaction by tracking number
    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<UserTransactionDto.TransactionResponseDto> getTransactionByTrackingNumber(
            @PathVariable String trackingNumber) {
        try {
            UserTransactionDto.TransactionResponseDto transaction =
                    transactionService.getTransactionByTrackingNumber(trackingNumber);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get user's transaction statistics
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<UserTransactionDto.TransactionStatsDto> getUserTransactionStats(
            @PathVariable Long userId) {
        try {
            UserTransactionDto.TransactionStatsDto stats =
                    transactionService.getUserTransactionStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create new transaction
    @PostMapping
    public ResponseEntity<UserTransactionDto.TransactionResponseDto> createTransaction(
            @RequestBody UserTransactionDto.TransactionCreateDto createDto) {
        try {
            UserTransactionDto.TransactionResponseDto transaction =
                    transactionService.createTransaction(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update transaction status
    @PutMapping("/{transactionId}/status")
    public ResponseEntity<UserTransactionDto.TransactionResponseDto> updateTransactionStatus(
            @PathVariable Long transactionId,
            @RequestBody UserTransactionDto.UpdateStatusDto updateDto) {
        try {
            UserTransactionDto.TransactionResponseDto transaction =
                    transactionService.updateTransactionStatus(transactionId, updateDto);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Cancel transaction
    @PutMapping("/{transactionId}/cancel")
    public ResponseEntity<UserTransactionDto.TransactionResponseDto> cancelTransaction(
            @PathVariable Long transactionId,
            @RequestParam Long userId,
            @RequestBody UserTransactionDto.CancelOrderDto cancelDto) {
        try {
            UserTransactionDto.TransactionResponseDto transaction =
                    transactionService.cancelTransaction(transactionId, userId, cancelDto);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get overdue transactions (admin endpoint)
    @GetMapping("/overdue")
    public ResponseEntity<List<UserTransactionDto.TransactionResponseDto>> getOverdueTransactions() {
        try {
            List<UserTransactionDto.TransactionResponseDto> overdueTransactions =
                    transactionService.getOverdueTransactions();
            return ResponseEntity.ok(overdueTransactions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update overdue transactions (admin endpoint)
    @PostMapping("/update-overdue")
    public ResponseEntity<String> updateOverdueTransactions() {
        try {
            transactionService.updateOverdueTransactions();
            return ResponseEntity.ok("Overdue transactions updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update overdue transactions");
        }
    }

    // Get transactions by status
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<Page<UserTransactionDto.TransactionResponseDto>> getTransactionsByStatus(
            @PathVariable Long userId,
            @PathVariable String status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        try {
            UserTransactionDto.TransactionFilterDto filter = new UserTransactionDto.TransactionFilterDto();
            filter.setStatus(status);
            filter.setPage(page);
            filter.setSize(size);

            Page<UserTransactionDto.TransactionResponseDto> transactions =
                    transactionService.getUserTransactions(userId, filter);

            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get transactions by type
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<Page<UserTransactionDto.TransactionResponseDto>> getTransactionsByType(
            @PathVariable Long userId,
            @PathVariable String type,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        try {
            UserTransactionDto.TransactionFilterDto filter = new UserTransactionDto.TransactionFilterDto();
            filter.setType(type);
            filter.setPage(page);
            filter.setSize(size);

            Page<UserTransactionDto.TransactionResponseDto> transactions =
                    transactionService.getUserTransactions(userId, filter);

            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("User Transaction Service is running");
    }
}