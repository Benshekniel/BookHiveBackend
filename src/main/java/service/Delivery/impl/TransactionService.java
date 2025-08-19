package service.Delivery.impl;

import model.entity.Transaction;
import model.entity.Delivery;
import model.entity.Hub;
import model.dto.Delivery.TransactionDto.*;
import model.repo.Delivery.TransactionRepository;
import model.repo.Delivery.DeliveryRepository;
import model.repo.Delivery.RouteRepository;
import model.repo.Delivery.HubRepository;
//import model.repo.BookRepo;
import model.repo.AllUsersRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final DeliveryRepository deliveryRepository;
    private final RouteRepository routeRepository;
    private final HubRepository hubRepository;
//    private final BookRepo bookRepository;
    private final AllUsersRepo allUsersRepo;

    public TransactionResponseDto createTransaction(TransactionCreateDto createDto) {
        // Validate required entities exist
        validateCreateTransactionRequest(createDto);

        Transaction transaction = buildTransaction(createDto);
        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("Created transaction with ID: {}", savedTransaction.getTransactionId());
        return convertToResponseDto(savedTransaction);
    }

    public List<TransactionResponseDto> getAllTransactions() {
        List<Object[]> results = transactionRepository.findAllTransactionsWithDetails();
        return results.stream()
                .map(this::convertToResponseDtoFromJoinQuery)
                .collect(Collectors.toList());
    }

    public Optional<TransactionResponseDto> getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(this::convertToResponseDto);
    }

    public List<TransactionResponseDto> getTransactionsByStatus(String status) {
        Transaction.TransactionStatus transactionStatus = Transaction.TransactionStatus.valueOf(status.toUpperCase());
        return transactionRepository.findByStatus(transactionStatus).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDto> getTransactionsByType(String type) {
        Transaction.TransactionType transactionType = Transaction.TransactionType.valueOf(type.toUpperCase());
        return transactionRepository.findByType(transactionType).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDto> getTransactionsByPaymentStatus(String paymentStatus) {
        Transaction.PaymentStatus status = Transaction.PaymentStatus.valueOf(paymentStatus.toUpperCase());
        return transactionRepository.findByPaymentStatus(status).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public HubRevenueDto getHubRevenue(Long hubId) {
        // Verify hub exists
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found with ID: " + hubId));

        // Get transactions related to this hub through deliveries
        List<Object[]> revenueData = transactionRepository.findHubRevenueData(hubId);

        BigDecimal totalRevenue = BigDecimal.ZERO;
        Long totalTransactions = 0L;
        Long completedTransactions = 0L;

        for (Object[] row : revenueData) {
            BigDecimal amount = (BigDecimal) row[0];
            Long count = ((Number) row[1]).longValue();
            String paymentStatus = (String) row[2];

            totalTransactions += count;
            if ("COMPLETED".equals(paymentStatus)) {
                totalRevenue = totalRevenue.add(amount != null ? amount : BigDecimal.ZERO);
                completedTransactions += count;
            }
        }

        HubRevenueDto dto = new HubRevenueDto();
        dto.setHubId(hubId);
        dto.setHubName(hub.getName());
        dto.setTotalRevenue(totalRevenue);
        dto.setTotalTransactions(totalTransactions);
        dto.setCompletedTransactions(completedTransactions);
        dto.setPendingTransactions(totalTransactions - completedTransactions);

        return dto;
    }

    public RevenueSummaryDto getRevenueSummary() {
        List<Object[]> summaryData = transactionRepository.getRevenueSummary();

        RevenueSummaryDto dto = new RevenueSummaryDto();
        dto.setTotalRevenue(BigDecimal.ZERO);
        dto.setTotalTransactions(0L);
        dto.setCompletedTransactions(0L);
        dto.setPendingTransactions(0L);
        dto.setFailedTransactions(0L);

        for (Object[] row : summaryData) {
            String paymentStatus = (String) row[0];
            BigDecimal totalAmount = (BigDecimal) row[1];
            Long count = ((Number) row[2]).longValue();

            dto.setTotalTransactions(dto.getTotalTransactions() + count);

            switch (paymentStatus) {
                case "COMPLETED":
                    dto.setTotalRevenue(dto.getTotalRevenue().add(totalAmount != null ? totalAmount : BigDecimal.ZERO));
                    dto.setCompletedTransactions(count);
                    break;
                case "PENDING":
                    dto.setPendingTransactions(count);
                    break;
                case "FAILED":
                    dto.setFailedTransactions(count);
                    break;
            }
        }

        return dto;
    }

    public List<TransactionResponseDto> getTransactionsByHub(Long hubId) {
        List<Object[]> results = transactionRepository.findTransactionsByHub(hubId);
        return results.stream()
                .map(this::convertToResponseDtoFromJoinQuery)
                .collect(Collectors.toList());
    }

    public List<TransactionStatsDto> getTransactionStats() {
        List<Object[]> stats = transactionRepository.getTransactionStatsByStatus();
        return stats.stream()
                .map(stat -> {
                    TransactionStatsDto dto = new TransactionStatsDto();
                    dto.setStatus((String) stat[0]);
                    dto.setCount(((Number) stat[1]).longValue());
                    dto.setTotalAmount((BigDecimal) stat[2]);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public TransactionResponseDto updateTransactionStatus(Long transactionId, String status) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Transaction.TransactionStatus newStatus = Transaction.TransactionStatus.valueOf(status.toUpperCase());
        transaction.setStatus(newStatus);
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToResponseDto(updatedTransaction);
    }

    public TransactionResponseDto updatePaymentStatus(Long transactionId, String paymentStatus) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Transaction.PaymentStatus newPaymentStatus = Transaction.PaymentStatus.valueOf(paymentStatus.toUpperCase());
        transaction.setPaymentStatus(newPaymentStatus);
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToResponseDto(updatedTransaction);
    }

    public void deleteTransaction(Long transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new RuntimeException("Transaction not found");
        }

        // Check if transaction has associated deliveries
        List<Delivery> deliveries = deliveryRepository.findByTransactionId(transactionId);
        if (!deliveries.isEmpty()) {
            throw new RuntimeException("Cannot delete transaction with associated deliveries");
        }

        transactionRepository.deleteById(transactionId);
    }

    private void validateCreateTransactionRequest(TransactionCreateDto createDto) {
        if (createDto.getBookId() == null) {
            throw new IllegalArgumentException("Book ID is required");
        }
        if (createDto.getBorrowerId() == null) {
            throw new IllegalArgumentException("Borrower ID is required");
        }
        if (createDto.getType() == null || createDto.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction type is required");
        }
        if (createDto.getPaymentAmount() == null || createDto.getPaymentAmount().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment amount is required");
        }
    }

    private Transaction buildTransaction(TransactionCreateDto createDto) {
        Transaction transaction = new Transaction();

        transaction.setBookId(createDto.getBookId());
        transaction.setBorrowerId(createDto.getBorrowerId());

        if (createDto.getLenderId() != null) {
            transaction.setBorrowerId(createDto.getLenderId()); // Note: Your entity might need a lenderId field
        }

        transaction.setType(Transaction.TransactionType.valueOf(createDto.getType().toUpperCase()));
        transaction.setStatus(Transaction.TransactionStatus.PENDING);
        transaction.setPaymentStatus(Transaction.PaymentStatus.PENDING);

        try {
            transaction.setPaymentAmount(new BigDecimal(createDto.getPaymentAmount()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid payment amount format");
        }

        transaction.setStartDate(createDto.getStartDate());
        transaction.setEndDate(createDto.getEndDate());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        return transaction;
    }

    private TransactionResponseDto convertToResponseDto(Transaction transaction) {
        TransactionResponseDto dto = new TransactionResponseDto();

        dto.setTransactionId(transaction.getTransactionId());
        dto.setBookId(transaction.getBookId());
        dto.setBorrowerId(transaction.getBorrowerId());
        dto.setType(transaction.getType().name());
        dto.setStatus(transaction.getStatus().name());
        dto.setPaymentStatus(transaction.getPaymentStatus().name());
        dto.setPaymentAmount(transaction.getPaymentAmount() != null ? transaction.getPaymentAmount().toString() : "0");
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setStartDate(transaction.getStartDate());
        dto.setEndDate(transaction.getEndDate());
        dto.setReturnDate(transaction.getReturnDate());

        // Get book details
//        if (transaction.getBookId() != null) {
//            bookRepository.findById(transaction.getBookId().intValue())
//                    .ifPresent(book -> dto.setBookTitle(book.getTitle()));
//        }

        // Get borrower details
        if (transaction.getBorrowerId() != null) {
            allUsersRepo.findById(transaction.getBorrowerId().intValue())
                    .ifPresent(user -> dto.setBorrowerName(user.getName()));
        }

        return dto;
    }

    private TransactionResponseDto convertToResponseDtoFromJoinQuery(Object[] result) {
        Transaction transaction = (Transaction) result[0];
        String bookTitle = result[1] != null ? result[1].toString() : "Unknown Book";
        String borrowerName = result[2] != null ? result[2].toString() : "Unknown User";

        TransactionResponseDto dto = convertToResponseDto(transaction);
        dto.setBookTitle(bookTitle);
        dto.setBorrowerName(borrowerName);

        return dto;
    }
}