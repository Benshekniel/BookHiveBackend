package service.User.impl;

import model.dto.UserTransactionDto;
import model.entity.Transaction;
import model.entity.Delivery;
import model.repo.User.UserTransactionRepository;
import model.repo.Delivery.DeliveryRepository; // Your existing delivery repo
import service.User.impl.DeliveryTrackingService;
import service.Delivery.impl.DeliveryService; // Your existing delivery service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserTransactionService {

    @Autowired
    private UserTransactionRepository transactionRepository;

    @Autowired
    private DeliveryTrackingService deliveryTrackingService;

    @Autowired
    private DeliveryRepository deliveryRepository; // Your existing delivery repo

    @Autowired
    private DeliveryService deliveryService; // Your existing delivery service

    // Mock data for users and books (replace with actual service calls)
    private Map<Long, UserTransactionDto.UserDetailsDto> mockUsers;
    private Map<Long, Map<String, String>> mockBooks;

    // Initialize mock data in constructor
    public UserTransactionService() {
        this.mockUsers = createMockUsers();
        this.mockBooks = createMockBooks();
    }

    public Page<UserTransactionDto.TransactionResponseDto> getUserTransactions(
            Long userId, UserTransactionDto.TransactionFilterDto filter) {

        Pageable pageable = createPageable(filter);
        Page<Transaction> transactions;

        try {
            // Use conditional logic to call appropriate repository method
            if (filter.getStatus() != null && filter.getType() != null) {
                // Both status and type filters
                Transaction.TransactionStatus status = Transaction.TransactionStatus.valueOf(filter.getStatus().toUpperCase());
                Transaction.TransactionType type = Transaction.TransactionType.valueOf(mapFrontendTypeToBackend(filter.getType()));
                transactions = transactionRepository.findByUserAndStatusAndType(userId, status, type, pageable);
            } else if (filter.getStatus() != null) {
                // Status filter only
                Transaction.TransactionStatus status = Transaction.TransactionStatus.valueOf(filter.getStatus().toUpperCase());
                transactions = transactionRepository.findByUserAndStatus(userId, status, pageable);
            } else if (filter.getType() != null) {
                // Type filter only
                String backendType = mapFrontendTypeToBackend(filter.getType());
                Transaction.TransactionType type = Transaction.TransactionType.valueOf(backendType);
                transactions = transactionRepository.findByUserAndType(userId, type, pageable);
            } else if (filter.getPaymentStatus() != null) {
                // Payment status filter only
                Transaction.PaymentStatus paymentStatus = Transaction.PaymentStatus.valueOf(filter.getPaymentStatus().toUpperCase());
                transactions = transactionRepository.findByUserAndPaymentStatus(userId, paymentStatus, pageable);
            } else if (filter.getFromDate() != null && filter.getToDate() != null) {
                // Date range filter only
                transactions = transactionRepository.findByUserAndDateRange(userId, filter.getFromDate(), filter.getToDate(), pageable);
            } else {
                // No filters
                transactions = transactionRepository.findByUserInvolved(userId, pageable);
            }
        } catch (IllegalArgumentException e) {
            // Handle invalid enum values
            System.err.println("Invalid filter values: " + e.getMessage());
            transactions = transactionRepository.findByUserInvolved(userId, pageable);
        } catch (Exception e) {
            // Fallback to native query
            System.err.println("Query failed, using native fallback: " + e.getMessage());
            transactions = transactionRepository.findByUserInvolvedNative(userId, pageable);

            // Apply in-memory filtering for complex cases
            if (hasFilters(filter)) {
                transactions = filterInMemory(transactions, filter);
            }
        }

        return transactions.map(this::convertToResponseDto);
    }

    // FIXED: Better stats handling
    public UserTransactionDto.TransactionStatsDto getUserTransactionStats(Long userId) {
        try {
            Object[] result = transactionRepository.getUserTransactionSummary(userId);

            if (result == null || result.length == 0) {
                System.out.println("No stats data found for user: " + userId);
                return createEmptyStats();
            }

            // The query returns an Object[] where the first element might be another Object[]
            Object firstElement = result[0];
            Object[] statsArray;

            if (firstElement instanceof Object[]) {
                // If the first element is an array, use it
                statsArray = (Object[]) firstElement;
            } else {
                // If not, use the result array directly
                statsArray = result;
            }

            UserTransactionDto.TransactionStatsDto statsDto = new UserTransactionDto.TransactionStatsDto();

            try {
                statsDto.setTotalOrders(extractLongValue(statsArray, 0));
                statsDto.setActiveOrders(extractLongValue(statsArray, 1));
                statsDto.setCompletedOrders(extractLongValue(statsArray, 2));
                statsDto.setOverdueOrders(extractLongValue(statsArray, 3));
                statsDto.setCancelledOrders(extractLongValue(statsArray, 4));
                statsDto.setBorrowedBooks(extractLongValue(statsArray, 5));
                statsDto.setPurchasedBooks(extractLongValue(statsArray, 6));
                statsDto.setWonAuctions(statsArray.length > 7 ? extractLongValue(statsArray, 7) : 0L);

                // These might need separate queries
                statsDto.setExchangedBooks(0L);
                statsDto.setTotalSpent(BigDecimal.ZERO);
                statsDto.setTotalEarned(BigDecimal.ZERO);

                System.out.println("Successfully parsed stats for user: " + userId);
                return statsDto;
            } catch (Exception parseEx) {
                System.err.println("Error parsing individual stat values: " + parseEx.getMessage());
                return createEmptyStats();
            }
        } catch (Exception e) {
            System.err.println("Error getting transaction stats for user " + userId + ": " + e.getMessage());
            return createEmptyStats();
        }
    }

    // Helper method to safely extract Long values
    private Long extractLongValue(Object[] result, int index) {
        if (index >= result.length || result[index] == null) {
            return 0L;
        }

        Object value = result[index];
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                System.err.println("Could not parse string to long: " + value);
                return 0L;
            }
        } else {
            System.err.println("Unexpected value type at index " + index + ": " + value.getClass().getSimpleName());
            return 0L;
        }
    }

    // Alternative method using individual queries (more reliable)
    public UserTransactionDto.TransactionStatsDto getUserTransactionStatsAlternative(Long userId) {
        try {
            UserTransactionDto.TransactionStatsDto stats = new UserTransactionDto.TransactionStatsDto();

            // Get total count
            Long totalCount = transactionRepository.countByUserAndStatus(userId, null);
            stats.setTotalOrders(totalCount != null ? totalCount : 0L);

            // Get counts by status
            stats.setActiveOrders(transactionRepository.countByUserAndStatus(userId, Transaction.TransactionStatus.ACTIVE));
            stats.setCompletedOrders(transactionRepository.countByUserAndStatus(userId, Transaction.TransactionStatus.COMPLETED));
            stats.setOverdueOrders(transactionRepository.countByUserAndStatus(userId, Transaction.TransactionStatus.OVERDUE));
            stats.setCancelledOrders(transactionRepository.countByUserAndStatus(userId, Transaction.TransactionStatus.CANCELLED));

            // Get counts by type
            stats.setBorrowedBooks(transactionRepository.countByUserAndType(userId, Transaction.TransactionType.LEND));
            stats.setPurchasedBooks(transactionRepository.countByUserAndType(userId, Transaction.TransactionType.SALE));
            stats.setWonAuctions(transactionRepository.countByUserAndType(userId, Transaction.TransactionType.BIDDING));
            stats.setExchangedBooks(transactionRepository.countByUserAndType(userId, Transaction.TransactionType.DONATION));

            // Set defaults for financial data
            stats.setTotalSpent(BigDecimal.ZERO);
            stats.setTotalEarned(BigDecimal.ZERO);

            return stats;
        } catch (Exception e) {
            System.err.println("Error in alternative stats method: " + e.getMessage());
            return createEmptyStats();
        }
    }

    // In-memory filtering as fallback
    private Page<Transaction> filterInMemory(Page<Transaction> transactions, UserTransactionDto.TransactionFilterDto filter) {
        List<Transaction> filtered = transactions.getContent().stream()
                .filter(t -> {
                    try {
                        // Status filter
                        if (filter.getStatus() != null) {
                            Transaction.TransactionStatus targetStatus = Transaction.TransactionStatus.valueOf(filter.getStatus().toUpperCase());
                            if (t.getStatus() != targetStatus) {
                                return false;
                            }
                        }

                        // Type filter
                        if (filter.getType() != null) {
                            String mappedType = mapFrontendTypeToBackend(filter.getType());
                            Transaction.TransactionType targetType = Transaction.TransactionType.valueOf(mappedType);
                            if (t.getType() != targetType) {
                                return false;
                            }
                        }

                        // Payment status filter
                        if (filter.getPaymentStatus() != null) {
                            Transaction.PaymentStatus targetPaymentStatus = Transaction.PaymentStatus.valueOf(filter.getPaymentStatus().toUpperCase());
                            if (t.getPaymentStatus() != targetPaymentStatus) {
                                return false;
                            }
                        }

                        // Date range filter
                        if (filter.getFromDate() != null && t.getCreatedAt().isBefore(filter.getFromDate())) {
                            return false;
                        }
                        if (filter.getToDate() != null && t.getCreatedAt().isAfter(filter.getToDate())) {
                            return false;
                        }

                        return true;
                    } catch (IllegalArgumentException e) {
                        // If enum conversion fails, exclude the transaction
                        return false;
                    }
                })
                .collect(Collectors.toList());

        return new org.springframework.data.domain.PageImpl<>(
                filtered,
                transactions.getPageable(),
                filtered.size() // Use filtered size, not original total
        );
    }

    private String mapFrontendTypeToBackend(String frontendType) {
        switch (frontendType.toLowerCase()) {
            case "purchase": return "SALE";
            case "borrow": return "LOAN";
            case "bidding": return "AUCTION";
            case "exchange": return "DONATION";
            default: return frontendType.toUpperCase();
        }
    }

    public UserTransactionDto.TransactionResponseDto getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + transactionId));
        return convertToResponseDto(transaction);
    }

    public UserTransactionDto.TransactionResponseDto getTransactionByTrackingNumber(String trackingNumber) {
        Transaction transaction = transactionRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Transaction not found with tracking number: " + trackingNumber));
        return convertToResponseDto(transaction);
    }

    public UserTransactionDto.TransactionResponseDto createTransaction(
            UserTransactionDto.TransactionCreateDto createDto) {

        Transaction transaction = new Transaction();

        // Basic fields
        transaction.setBookId(createDto.getBookId());
        transaction.setUserId(determineUserId(createDto));
        transaction.setBorrowerId(createDto.getBorrowerId());
        transaction.setSellerId(createDto.getSellerId());
        transaction.setLenderId(createDto.getLenderId());
        transaction.setExchangerId(createDto.getExchangerId());

        // Parse and set type
        transaction.setType(Transaction.TransactionType.valueOf(createDto.getType().toUpperCase()));
        transaction.setStatus(Transaction.TransactionStatus.PENDING);
        transaction.setPaymentStatus(Transaction.PaymentStatus.PENDING);

        // Dates
        transaction.setStartDate(createDto.getStartDate());
        transaction.setEndDate(createDto.getEndDate());
        transaction.setEstimatedDelivery(createDto.getEstimatedDelivery());

        // Amounts
        transaction.setPaymentAmount(createDto.getPaymentAmount());
        transaction.setSecurityDeposit(createDto.getSecurityDeposit());
        transaction.setDeliveryAmount(createDto.getDeliveryAmount());
        transaction.setWinningBid(createDto.getWinningBid());

        // Delivery
        if (createDto.getDeliveryMethod() != null) {
            transaction.setDeliveryMethod(
                    Transaction.DeliveryMethod.valueOf(createDto.getDeliveryMethod().toUpperCase()));
        }
        transaction.setDeliveryAddress(createDto.getDeliveryAddress());

        // Payment
        if (createDto.getPaymentMethod() != null) {
            transaction.setPaymentMethod(
                    Transaction.PaymentMethod.valueOf(createDto.getPaymentMethod().toUpperCase()));
        }

        // Periods
        transaction.setBorrowPeriod(createDto.getBorrowPeriod());
        transaction.setExchangePeriod(createDto.getExchangePeriod());
        transaction.setAuctionEndDate(createDto.getAuctionEndDate());

        // Generate tracking number
        transaction.setTrackingNumber(generateTrackingNumber());

        Transaction saved = transactionRepository.save(transaction);
        return convertToResponseDto(saved);
    }

    public UserTransactionDto.TransactionResponseDto updateTransactionStatus(
            Long transactionId, UserTransactionDto.UpdateStatusDto updateDto) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Transaction.TransactionStatus newStatus =
                Transaction.TransactionStatus.valueOf(updateDto.getStatus().toUpperCase());

        transaction.setStatus(newStatus);

        // Update specific fields based on status
        if (newStatus == Transaction.TransactionStatus.COMPLETED) {
            transaction.setActualDelivery(LocalDateTime.now());
            if (transaction.getPaymentStatus() == Transaction.PaymentStatus.PENDING) {
                transaction.setPaymentStatus(Transaction.PaymentStatus.COMPLETED);
            }
        } else if (newStatus == Transaction.TransactionStatus.OVERDUE) {
            calculateOverdueDays(transaction);
        }

        Transaction saved = transactionRepository.save(transaction);
        return convertToResponseDto(saved);
    }

    public UserTransactionDto.TransactionResponseDto cancelTransaction(
            Long transactionId, Long userId, UserTransactionDto.CancelOrderDto cancelDto) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Verify user can cancel
        if (!transactionRepository.canUserCancelTransaction(transactionId, userId)) {
            throw new RuntimeException("User not authorized to cancel this transaction or transaction cannot be cancelled");
        }

        // Calculate refund
        RefundCalculation refund = calculateRefund(transaction);

        // Update transaction
        transaction.setStatus(Transaction.TransactionStatus.CANCELLED);
        transaction.setCancelReason(cancelDto.getReason());
        transaction.setCancelDate(LocalDate.now());
        transaction.setRefundAmount(refund.getRefundAmount());
        transaction.setDeductionAmount(refund.getDeductionAmount());

        // Update payment status
        if (refund.getRefundAmount().compareTo(BigDecimal.ZERO) > 0) {
            transaction.setPaymentStatus(Transaction.PaymentStatus.REFUNDED);
        }

        Transaction saved = transactionRepository.save(transaction);
        return convertToResponseDto(saved);
    }

    public List<UserTransactionDto.TransactionResponseDto> getOverdueTransactions() {
        List<Transaction> overdueTransactions =
                transactionRepository.findOverdueTransactions(LocalDateTime.now());

        return overdueTransactions.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public void updateOverdueTransactions() {
        List<Transaction> overdueTransactions =
                transactionRepository.findOverdueTransactions(LocalDateTime.now());

        overdueTransactions.forEach(transaction -> {
            transaction.setStatus(Transaction.TransactionStatus.OVERDUE);
            calculateOverdueDays(transaction);
            transactionRepository.save(transaction);
        });
    }

    // Helper methods
    private UserTransactionDto.TransactionResponseDto convertToResponseDto(Transaction transaction) {
        UserTransactionDto.TransactionResponseDto dto = new UserTransactionDto.TransactionResponseDto();

        // Basic fields
        dto.setTransactionId(transaction.getTransactionId());
        dto.setOrderId("ORD" + String.format("%03d", transaction.getTransactionId()));
        dto.setType(mapTransactionType(transaction.getType()));
        dto.setStatus(mapTransactionStatus(transaction.getStatus()));
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setOrderDate(transaction.getCreatedAt());
        dto.setStartDate(transaction.getStartDate());
        dto.setEndDate(transaction.getEndDate());
        dto.setReturnDate(transaction.getReturnDate());
        dto.setEstimatedDelivery(transaction.getEstimatedDelivery());
        dto.setActualDelivery(transaction.getActualDelivery());

        // Amounts
        dto.setPaymentAmount(transaction.getPaymentAmount());
        dto.setTotalAmount(calculateTotalAmount(transaction));
        dto.setPaymentStatus(transaction.getPaymentStatus() != null ?
                transaction.getPaymentStatus().name() : null);
        dto.setPaymentMethod(transaction.getPaymentMethod() != null ?
                transaction.getPaymentMethod().name() : null);

        // Book details - Enhanced with better fallbacks
        dto.setBookId(transaction.getBookId());
        Map<String, String> bookDetails = mockBooks.get(transaction.getBookId());
        if (bookDetails != null) {
            dto.setBookTitle(bookDetails.get("title"));
            dto.setBookAuthor(bookDetails.get("author"));
            dto.setBookCover(bookDetails.get("cover"));
        } else {
            // Fallback book details
            dto.setBookTitle("Book " + transaction.getBookId());
            dto.setBookAuthor("Unknown Author");
            dto.setBookCover(generateFallbackBookImage(transaction.getBookId()));
        }

        // User details based on transaction type
        setUserDetails(dto, transaction);

        // Delivery details
        dto.setDeliveryMethod(transaction.getDeliveryMethod() != null ?
                transaction.getDeliveryMethod().name().toLowerCase() : "delivery");
        dto.setDeliveryAddress(transaction.getDeliveryAddress());
        dto.setTrackingNumber(transaction.getTrackingNumber());

        // Type-specific fields
        dto.setBorrowPeriod(transaction.getBorrowPeriod());
        dto.setSecurityDeposit(transaction.getSecurityDeposit());
        dto.setOverdueDays(transaction.getOverdueDays());
        dto.setExchangePeriod(transaction.getExchangePeriod());
        dto.setWinningBid(transaction.getWinningBid());
        dto.setAuctionEndDate(transaction.getAuctionEndDate());
        dto.setDeliveryAmount(transaction.getDeliveryAmount());

        // Cancellation details
        dto.setCancelReason(transaction.getCancelReason());
        dto.setCancelDate(transaction.getCancelDate());
        dto.setRefundAmount(transaction.getRefundAmount());
        dto.setDeductionAmount(transaction.getDeductionAmount());

        // ✅ UPDATED: Generate tracking information using real delivery data
        dto.setTracking(generateTrackingInfo(transaction));

        return dto;
    }

    // ✅ UPDATED: Use real delivery tracking instead of mock data
    private List<UserTransactionDto.TrackingDto> generateTrackingInfo(Transaction transaction) {
        List<UserTransactionDto.TrackingDto> trackingList = new ArrayList<>();

        try {
            // Get real tracking info from delivery service using your existing delivery system
            List<DeliveryTrackingService.TrackingInfo> deliveryTracking =
                    deliveryTrackingService.getTrackingInfoByTransactionId(transaction.getTransactionId());

            // Convert to DTO format
            for (DeliveryTrackingService.TrackingInfo info : deliveryTracking) {
                UserTransactionDto.TrackingDto trackingDto = new UserTransactionDto.TrackingDto();
                trackingDto.setStatus(info.getStatus());
                trackingDto.setTimestamp(info.getTimestamp());
                trackingDto.setDescription(info.getDescription());
                trackingList.add(trackingDto);
            }

            // Add transaction-specific information
            addTransactionSpecificTracking(trackingList, transaction);

        } catch (Exception e) {
            System.err.println("Error getting delivery tracking, using fallback: " + e.getMessage());
            // Fallback to basic tracking
            trackingList = generateFallbackTrackingInfo(transaction);
        }

        return trackingList;
    }

    private void addTransactionSpecificTracking(List<UserTransactionDto.TrackingDto> trackingList, Transaction transaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");

        // Add transaction-specific tracking based on type and status
        if (transaction.getType() == Transaction.TransactionType.LEND &&
                transaction.getStatus() == Transaction.TransactionStatus.OVERDUE) {

            trackingList.add(createTrackingEntry(
                    "Overdue",
                    LocalDateTime.now().format(formatter),
                    "Book return is overdue by " + (transaction.getOverdueDays() != null ? transaction.getOverdueDays() : 0) + " days"
            ));
        }

        if (transaction.getStatus() == Transaction.TransactionStatus.CANCELLED) {
            trackingList.add(createTrackingEntry(
                    "Order Cancelled",
                    (transaction.getCancelDate() != null ?
                            transaction.getCancelDate().atStartOfDay() : LocalDateTime.now()).format(formatter),
                    "Order cancelled. " +
                            (transaction.getRefundAmount() != null && transaction.getRefundAmount().compareTo(BigDecimal.ZERO) > 0 ?
                                    "Refund of Rs. " + transaction.getRefundAmount() + " will be processed within 3-5 business days." :
                                    "")
            ));
        }
    }

    private List<UserTransactionDto.TrackingDto> generateFallbackTrackingInfo(Transaction transaction) {
        List<UserTransactionDto.TrackingDto> tracking = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");

        // Basic tracking as fallback
        tracking.add(createTrackingEntry(
                "Order Placed",
                transaction.getCreatedAt().format(formatter),
                "Your order has been confirmed"
        ));

        // Status-based tracking
        switch (transaction.getStatus()) {
            case PENDING:
                tracking.add(createTrackingEntry(
                        "Awaiting Confirmation",
                        transaction.getCreatedAt().format(formatter),
                        "Waiting for seller confirmation"
                ));
                break;
            case ACTIVE:
                tracking.add(createTrackingEntry(
                        "Processing",
                        transaction.getCreatedAt().plusHours(1).format(formatter),
                        "Order is being processed"
                ));
                break;
            case COMPLETED:
                if (transaction.getActualDelivery() != null) {
                    tracking.add(createTrackingEntry(
                            "Delivered",
                            transaction.getActualDelivery().format(formatter),
                            "Order delivered successfully"
                    ));
                }
                break;
        }

        return tracking;
    }

    // Enhanced book image fallback - FIXED VERSION
    private String generateFallbackBookImage(Long bookId) {
        // Generate a simple data URL for SVG without String.format
        String title = "Book " + bookId;
        StringBuilder svg = new StringBuilder();
        svg.append("data:image/svg+xml,");
        svg.append("%3csvg width='150' height='200' xmlns='http://www.w3.org/2000/svg'%3e");
        svg.append("%3crect width='150' height='200' fill='%234F46E5'/%3e");
        svg.append("%3ctext x='75' y='100' text-anchor='middle' fill='%23FFFFFF' font-size='12'%3e");
        svg.append(title.replace(" ", "%20"));
        svg.append("%3c/text%3e");
        svg.append("%3c/svg%3e");
        return svg.toString();
    }

    private void setUserDetails(UserTransactionDto.TransactionResponseDto dto, Transaction transaction) {
        if (transaction.getSellerId() != null) {
            dto.setSeller(mockUsers.get(transaction.getSellerId()));
        }
        if (transaction.getLenderId() != null) {
            dto.setLender(mockUsers.get(transaction.getLenderId()));
        }
        if (transaction.getExchangerId() != null) {
            dto.setExchanger(mockUsers.get(transaction.getExchangerId()));
        }
        if (transaction.getBorrowerId() != null) {
            dto.setBorrower(mockUsers.get(transaction.getBorrowerId()));
        }
    }

    private UserTransactionDto.TrackingDto createTrackingEntry(String status, String timestamp, String description) {
        UserTransactionDto.TrackingDto tracking = new UserTransactionDto.TrackingDto();
        tracking.setStatus(status);
        tracking.setTimestamp(timestamp);
        tracking.setDescription(description);
        return tracking;
    }

    private String mapTransactionType(Transaction.TransactionType type) {
        switch (type) {
            case SALE: return "purchase";
            case LEND: return "borrow";
            case BIDDING: return "bidding";
            case DONATION: return "exchange"; // Map donation to exchange for frontend
            default: return type.name().toLowerCase();
        }
    }

    private String mapTransactionStatus(Transaction.TransactionStatus status) {
        switch (status) {
            case COMPLETED: return "delivered";
            default: return status.name().toLowerCase();
        }
    }

    private BigDecimal calculateTotalAmount(Transaction transaction) {
        BigDecimal total = transaction.getPaymentAmount() != null ?
                transaction.getPaymentAmount() : BigDecimal.ZERO;

        if (transaction.getDeliveryAmount() != null) {
            total = total.add(transaction.getDeliveryAmount());
        }

        return total;
    }

    private void calculateOverdueDays(Transaction transaction) {
        if (transaction.getEndDate() != null) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(
                    transaction.getEndDate().toLocalDate(),
                    LocalDate.now()
            );
            transaction.setOverdueDays((int) Math.max(0, days));
        }
    }

    private RefundCalculation calculateRefund(Transaction transaction) {
        BigDecimal baseAmount = transaction.getPaymentAmount() != null ?
                transaction.getPaymentAmount() : BigDecimal.ZERO;
        BigDecimal deductionAmount = BigDecimal.ZERO;

        // Time-based deductions
        if (transaction.getCreatedAt() != null) {
            long hours = java.time.temporal.ChronoUnit.HOURS.between(
                    transaction.getCreatedAt(), LocalDateTime.now());

            if (hours > 24) {
                // 10% processing fee after 24 hours
                BigDecimal processingFee = baseAmount.multiply(new BigDecimal("0.1"));
                deductionAmount = deductionAmount.add(processingFee);
            }
        }

        // Status-based deductions
        if (transaction.getStatus() == Transaction.TransactionStatus.ACTIVE) {
            deductionAmount = deductionAmount.add(new BigDecimal("150")); // Shipping fee
        }

        BigDecimal refundAmount = baseAmount.subtract(deductionAmount);
        refundAmount = refundAmount.max(BigDecimal.ZERO);

        return new RefundCalculation(refundAmount, deductionAmount);
    }

    private Long determineUserId(UserTransactionDto.TransactionCreateDto createDto) {
        // Determine primary user based on transaction type
        if (createDto.getBorrowerId() != null) return createDto.getBorrowerId();
        if (createDto.getSellerId() != null) return createDto.getSellerId();
        if (createDto.getLenderId() != null) return createDto.getLenderId();
        if (createDto.getExchangerId() != null) return createDto.getExchangerId();
        return null;
    }

    private String generateTrackingNumber() {
        return "BH" + System.currentTimeMillis();
    }

    private String mapSortField(String frontendField) {
        // Map frontend sort fields to actual entity fields
        switch (frontendField) {
            case "createdAt":
            case "created_at":
            case "orderDate":
                return "createdAt";
            case "updatedAt":
            case "updated_at":
                return "updatedAt";
            case "startDate":
            case "start_date":
                return "startDate";
            case "endDate":
            case "end_date":
                return "endDate";
            default:
                return "createdAt"; // Safe default
        }
    }

    private Pageable createPageable(UserTransactionDto.TransactionFilterDto filter) {
        String sortField = mapSortField(filter.getSortBy());
        Sort sort = Sort.by(
                "DESC".equalsIgnoreCase(filter.getSortDirection()) ?
                        Sort.Direction.DESC : Sort.Direction.ASC,
                sortField
        );
        return PageRequest.of(filter.getPage(), filter.getSize(), sort);
    }

    private boolean hasFilters(UserTransactionDto.TransactionFilterDto filter) {
        return filter.getStatus() != null || filter.getType() != null ||
                filter.getPaymentStatus() != null || filter.getFromDate() != null ||
                filter.getToDate() != null;
    }

    private UserTransactionDto.TransactionStatsDto createEmptyStats() {
        UserTransactionDto.TransactionStatsDto stats = new UserTransactionDto.TransactionStatsDto();
        stats.setTotalOrders(0L);
        stats.setActiveOrders(0L);
        stats.setCompletedOrders(0L);
        stats.setOverdueOrders(0L);
        stats.setCancelledOrders(0L);
        stats.setBorrowedBooks(0L);
        stats.setPurchasedBooks(0L);
        stats.setExchangedBooks(0L);
        stats.setWonAuctions(0L);
        stats.setTotalSpent(BigDecimal.ZERO);
        stats.setTotalEarned(BigDecimal.ZERO);
        return stats;
    }

    // Mock data creation methods - FIXED VERSION
    private Map<Long, UserTransactionDto.UserDetailsDto> createMockUsers() {
        Map<Long, UserTransactionDto.UserDetailsDto> users = new HashMap<>();

        for (long i = 2001; i <= 2015; i++) {
            UserTransactionDto.UserDetailsDto user = new UserTransactionDto.UserDetailsDto();
            user.setUserId(i);
            user.setName("User " + i);
            user.setEmail("user" + i + "@example.com");
            user.setPhone("+94 77 123 " + String.format("%04d", i));
            user.setLocation("Colombo " + (i % 10));
            users.put(i, user);
        }

        return users;
    }

    private Map<Long, Map<String, String>> createMockBooks() {
        Map<Long, Map<String, String>> books = new HashMap<>();

        String[] titles = {
                "Advanced Java Programming", "React Development Guide", "Spring Boot Mastery",
                "Database Design Principles", "Web Development Essentials", "Python for Beginners",
                "Data Structures & Algorithms", "Machine Learning Basics", "Cloud Computing Guide",
                "Mobile App Development", "Software Engineering", "Computer Networks",
                "Operating Systems", "Artificial Intelligence", "Cybersecurity Fundamentals"
        };

        String[] authors = {
                "John Smith", "Jane Doe", "Bob Wilson", "Alice Johnson", "Charlie Brown",
                "Diana Prince", "Edward Norton", "Fiona Clark", "George Lucas", "Helen Troy",
                "Ian Fleming", "Julia Roberts", "Kevin Spacey", "Laura Palmer", "Mike Ross"
        };

        for (long i = 101; i <= 160; i++) {
            Map<String, String> book = new HashMap<>();
            int index = (int) (i - 101) % titles.length;
            book.put("title", titles[index]);
            book.put("author", authors[index]);
            // Use simple SVG generation
            book.put("cover", generateSimpleBookCover(titles[index], authors[index], i));
            books.put(i, book);
        }

        return books;
    }

    // FIXED: Simple book cover generation without String.format issues
    private String generateSimpleBookCover(String title, String author, Long bookId) {
        // Create a reliable SVG without complex formatting
        StringBuilder svg = new StringBuilder();

        // Clean the title and author for SVG
        String cleanTitle = title.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
        String cleanAuthor = author.replaceAll("[^a-zA-Z0-9\\s]", "").trim();

        // Truncate if too long
        if (cleanTitle.length() > 15) {
            cleanTitle = cleanTitle.substring(0, 15) + "...";
        }
        if (cleanAuthor.length() > 20) {
            cleanAuthor = cleanAuthor.substring(0, 20) + "...";
        }

        // URL encode the text
        cleanTitle = cleanTitle.replace(" ", "%20");
        cleanAuthor = cleanAuthor.replace(" ", "%20");

        // Build SVG data URL
        svg.append("data:image/svg+xml,");
        svg.append("%3csvg%20width='300'%20height='400'%20xmlns='http://www.w3.org/2000/svg'%3e");
        svg.append("%3cdefs%3e");
        svg.append("%3clinearGradient%20id='grad'%20x1='0%25'%20y1='0%25'%20x2='100%25'%20y2='100%25'%3e");
        svg.append("%3cstop%20offset='0%25'%20style='stop-color:%234F46E5;stop-opacity:1'%20/%3e");
        svg.append("%3cstop%20offset='100%25'%20style='stop-color:%236366F1;stop-opacity:1'%20/%3e");
        svg.append("%3c/linearGradient%3e");
        svg.append("%3c/defs%3e");
        svg.append("%3crect%20width='300'%20height='400'%20fill='url(%23grad)'/%3e");
        svg.append("%3ctext%20x='150'%20y='200'%20text-anchor='middle'%20fill='%23FFFFFF'%20font-size='16'%20font-weight='bold'%3e");
        svg.append(cleanTitle);
        svg.append("%3c/text%3e");
        svg.append("%3ctext%20x='150'%20y='240'%20text-anchor='middle'%20fill='%23E5E7EB'%20font-size='12'%3e");
        svg.append(cleanAuthor);
        svg.append("%3c/text%3e");
        svg.append("%3c/svg%3e");

        return svg.toString();
    }

    // Helper class for refund calculation
    private static class RefundCalculation {
        private final BigDecimal refundAmount;
        private final BigDecimal deductionAmount;

        public RefundCalculation(BigDecimal refundAmount, BigDecimal deductionAmount) {
            this.refundAmount = refundAmount;
            this.deductionAmount = deductionAmount;
        }

        public BigDecimal getRefundAmount() { return refundAmount; }
        public BigDecimal getDeductionAmount() { return deductionAmount; }
    }
}