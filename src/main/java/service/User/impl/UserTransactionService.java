package service.User.impl;

import model.dto.UserTransactionDto;
import model.entity.Transaction;
import model.entity.Delivery;
import model.entity.Users;
import model.entity.UserBooks;
import model.repo.User.UserTransactionRepository;
import model.repo.Delivery.DeliveryRepository;
import model.repo.UsersRepo;
import model.repo.UserBooksRepo;
import service.User.impl.DeliveryTrackingService;
import service.Delivery.impl.DeliveryService;
import service.GoogleDriveUpload.FileStorageService; // Add this import
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
    private DeliveryRepository deliveryRepository;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private UserBooksRepo userBooksRepo;

    @Autowired
    private FileStorageService fileStorageService; // Add this

    public Page<UserTransactionDto.TransactionResponseDto> getUserTransactions(
            Long userId, UserTransactionDto.TransactionFilterDto filter) {

        Pageable pageable = createPageable(filter);
        Page<Transaction> transactions;

        try {
            // Use conditional logic to call appropriate repository method
            if (filter.getStatus() != null && filter.getType() != null) {
                // Both status and type filters
                Transaction.TransactionStatus status = mapFrontendStatusToBackend(filter.getStatus());
                Transaction.TransactionType type = Transaction.TransactionType.valueOf(mapFrontendTypeToBackend(filter.getType()));
                transactions = transactionRepository.findByUserAndStatusAndType(userId, status, type, pageable);
            } else if (filter.getStatus() != null) {
                // Status filter only - UPDATED: Handle completed status properly
                if ("completed".equalsIgnoreCase(filter.getStatus()) || "delivered".equalsIgnoreCase(filter.getStatus())) {
                    // Get all transactions and filter by delivery status
                    transactions = transactionRepository.findByUserInvolved(userId, pageable);
                    transactions = filterByDeliveryStatus(transactions, Delivery.DeliveryStatus.DELIVERED);
                } else {
                    Transaction.TransactionStatus status = mapFrontendStatusToBackend(filter.getStatus());
                    transactions = transactionRepository.findByUserAndStatus(userId, status, pageable);
                }
            } else if (filter.getType() != null) {
                // Type filter only
                String backendType = mapFrontendTypeToBackend(filter.getType());
                Transaction.TransactionType type = Transaction.TransactionType.valueOf(backendType);
                transactions = transactionRepository.findByUserAndType(userId, type, pageable);
            } else if (filter.getPaymentStatus() != null) {
                Transaction.PaymentStatus paymentStatus = Transaction.PaymentStatus.valueOf(filter.getPaymentStatus().toUpperCase());
                transactions = transactionRepository.findByUserAndPaymentStatus(userId, paymentStatus, pageable);
            } else if (filter.getFromDate() != null && filter.getToDate() != null) {
                transactions = transactionRepository.findByUserAndDateRange(userId, filter.getFromDate(), filter.getToDate(), pageable);
            } else {
                transactions = transactionRepository.findByUserInvolved(userId, pageable);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid filter values: " + e.getMessage());
            transactions = transactionRepository.findByUserInvolved(userId, pageable);
        } catch (Exception e) {
            System.err.println("Query failed, using native fallback: " + e.getMessage());
            transactions = transactionRepository.findByUserInvolvedNative(userId, pageable);

            if (hasFilters(filter)) {
                transactions = filterInMemory(transactions, filter);
            }
        }

        return transactions.map(this::convertToResponseDto);
    }

    // ✅ FIXED: Map frontend status to backend enum values
    private Transaction.TransactionStatus mapFrontendStatusToBackend(String frontendStatus) {
        switch (frontendStatus.toLowerCase()) {
            case "active":
            case "pending":
                return Transaction.TransactionStatus.PENDING;
            case "completed":
            case "delivered":
                return Transaction.TransactionStatus.COMPLETED;
            case "cancelled":
                return Transaction.TransactionStatus.CANCELLED;
            case "overdue":
                return Transaction.TransactionStatus.OVERDUE;
            default:
                return Transaction.TransactionStatus.valueOf(frontendStatus.toUpperCase());
        }
    }

    // UPDATED: Filter by delivery status using your existing repository
    private Page<Transaction> filterByDeliveryStatus(Page<Transaction> transactions, Delivery.DeliveryStatus deliveryStatus) {
        List<Transaction> filtered = transactions.getContent().stream()
                .filter(transaction -> {
                    try {
                        List<Delivery> deliveries = deliveryRepository.findByTransactionId(transaction.getTransactionId());
                        return !deliveries.isEmpty() &&
                                deliveries.get(0).getStatus() == deliveryStatus;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        return new org.springframework.data.domain.PageImpl<>(
                filtered,
                transactions.getPageable(),
                filtered.size()
        );
    }

    // UPDATED: New type mapping according to your requirements
    private String mapFrontendTypeToBackend(String frontendType) {
        switch (frontendType.toLowerCase()) {
            case "purchased": return "SALE";        // purchasedBooks
            case "borrowed": return "LEND";         // borrowedBooks
            case "bidding": return "BIDDING";       // wonAuctions
            case "exchanged": return "EXCHANGE";    // exchangedBooks
            case "purchase": return "SALE";
            case "borrow": return "LEND";
            case "exchange": return "EXCHANGE";
            default: return frontendType.toUpperCase();
        }
    }

    // UPDATED: Enhanced stats with correct mapping
    public UserTransactionDto.TransactionStatsDto getUserTransactionStats(Long userId) {
        try {
            UserTransactionDto.TransactionStatsDto stats = new UserTransactionDto.TransactionStatsDto();

            // Get all transactions for the user using native query for reliability
            Page<Transaction> allTransactionsPage = transactionRepository.findByUserInvolvedNative(userId, Pageable.unpaged());
            List<Transaction> allTransactions = allTransactionsPage.getContent();

            stats.setTotalOrders((long) allTransactions.size());

            // Count by type - UPDATED mapping according to your requirements
            long purchasedBooks = allTransactions.stream()
                    .filter(t -> t.getType() == Transaction.TransactionType.SALE)
                    .count();
            stats.setPurchasedBooks(purchasedBooks);

            long borrowedBooks = allTransactions.stream()
                    .filter(t -> t.getType() == Transaction.TransactionType.LEND)
                    .count();
            stats.setBorrowedBooks(borrowedBooks);

            long wonAuctions = allTransactions.stream()
                    .filter(t -> t.getType() == Transaction.TransactionType.BIDDING)
                    .count();
            stats.setWonAuctions(wonAuctions);

            long exchangedBooks = allTransactions.stream()
                    .filter(t -> t.getType() == Transaction.TransactionType.EXCHANGE)
                    .count();
            stats.setExchangedBooks(exchangedBooks);

            // Count by status - FIXED: Use correct enum values
            long activeOrders = allTransactions.stream()
                    .filter(t -> t.getStatus() == Transaction.TransactionStatus.PENDING)
                    .count();
            stats.setActiveOrders(activeOrders);

            // UPDATED: Count completed as delivered in delivery table
            long completedOrders = allTransactions.stream()
                    .filter(this::isTransactionCompleted)
                    .count();
            stats.setCompletedOrders(completedOrders);

            long overdueOrders = allTransactions.stream()
                    .filter(t -> t.getStatus() == Transaction.TransactionStatus.OVERDUE)
                    .count();
            stats.setOverdueOrders(overdueOrders);

            long cancelledOrders = allTransactions.stream()
                    .filter(t -> t.getStatus() == Transaction.TransactionStatus.CANCELLED)
                    .count();
            stats.setCancelledOrders(cancelledOrders);

            // Financial data
            stats.setTotalSpent(BigDecimal.ZERO);
            stats.setTotalEarned(BigDecimal.ZERO);

            return stats;
        } catch (Exception e) {
            System.err.println("Error getting transaction stats: " + e.getMessage());
            return createEmptyStats();
        }
    }

    // Check if transaction is completed based on delivery status
    private boolean isTransactionCompleted(Transaction transaction) {
        try {
            List<Delivery> deliveries = deliveryRepository.findByTransactionId(transaction.getTransactionId());
            return !deliveries.isEmpty() &&
                    deliveries.get(0).getStatus() == Delivery.DeliveryStatus.DELIVERED;
        } catch (Exception e) {
            return transaction.getStatus() == Transaction.TransactionStatus.COMPLETED;
        }
    }

    // ✅ NEW: Get delivery status for transaction
    private String getDeliveryStatus(Transaction transaction) {
        try {
            List<Delivery> deliveries = deliveryRepository.findByTransactionId(transaction.getTransactionId());
            if (!deliveries.isEmpty()) {
                return mapDeliveryStatusToSimple(deliveries.get(0).getStatus());
            }
        } catch (Exception e) {
            System.err.println("Error getting delivery status: " + e.getMessage());
        }
        // Fallback to transaction status
        return mapTransactionStatusToDelivery(transaction.getStatus());
    }

    // ✅ NEW: Map delivery status to 4 simple states
    private String mapDeliveryStatusToSimple(Delivery.DeliveryStatus status) {
        switch (status) {
            case PLACED:
            case PENDING:
                return "Order Placed";
            case PICKED_UP_FROM_HOME:
            case PICKED_UP:
            case ASSIGNED:
                return "Picked Up";
            case IN_TRANSIT_TO_LOCAL_HUB:
            case AT_LOCAL_HUB:
            case IN_TRANSIT_TO_MAIN_HUB:
            case AT_MAIN_HUB:
            case IN_TRANSIT_TO_PROPER_LOCAL_HUB:
            case IN_TRANSIT:
                return "In Transit";
            case DELIVERED:
                return "Delivered";
            case CANCELLED:
                return "Cancelled";
            case FAILED:
                return "Failed";
            case DELAYED:
                return "Delayed";
            default:
                return "Order Placed";
        }
    }

    // ✅ NEW: Fallback mapping from transaction status
    private String mapTransactionStatusToDelivery(Transaction.TransactionStatus status) {
        switch (status) {
            case PENDING:
                return "Order Placed";
            case COMPLETED:
                return "Delivered";
            case CANCELLED:
                return "Cancelled";
            case OVERDUE:
                return "In Transit"; // Assume it's in transit if overdue
            default:
                return "Order Placed";
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

        Transaction.TransactionStatus newStatus = mapFrontendStatusToBackend(updateDto.getStatus());

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

    // UPDATED: Enhanced conversion with real user and book data
    private UserTransactionDto.TransactionResponseDto convertToResponseDto(Transaction transaction) {
        UserTransactionDto.TransactionResponseDto dto = new UserTransactionDto.TransactionResponseDto();

        // Basic fields
        dto.setTransactionId(transaction.getTransactionId());
        dto.setOrderId("ORD" + String.format("%05d", transaction.getTransactionId()));
        dto.setType(mapTransactionType(transaction.getType()));

        // ✅ UPDATED: Use delivery status instead of transaction status
        dto.setStatus(getDeliveryStatus(transaction));

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

        // UPDATED: Real book details with proper image handling
        setRealBookDetails(dto, transaction.getBookId());

        // UPDATED: Real user details
        setRealUserDetails(dto, transaction);

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

        // UPDATED: Real tracking information using your existing service
        dto.setTracking(generateTrackingInfo(transaction));

        return dto;
    }

    // UPDATED: Use your existing DeliveryTrackingService
    private List<UserTransactionDto.TrackingDto> generateTrackingInfo(Transaction transaction) {
        List<UserTransactionDto.TrackingDto> trackingList = new ArrayList<>();

        try {
            // Get tracking info from your existing service
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

        tracking.add(createTrackingEntry(
                "Order Placed",
                transaction.getCreatedAt().format(formatter),
                "Your order has been confirmed"
        ));

        switch (transaction.getStatus()) {
            case PENDING:
                tracking.add(createTrackingEntry(
                        "Awaiting Confirmation",
                        transaction.getCreatedAt().format(formatter),
                        "Waiting for seller confirmation"
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

    // ✅ UPDATED: Get real book details with proper image handling using FileStorageService
// ✅ UPDATED: Get real book details with proper image handling using your working method
    private void setRealBookDetails(UserTransactionDto.TransactionResponseDto dto, Long bookId) {
        try {
            Optional<UserBooks> bookOpt = userBooksRepo.findById(bookId);
            if (bookOpt.isPresent()) {
                UserBooks book = bookOpt.get();
                dto.setBookId(bookId);
                dto.setBookTitle(book.getTitle());
                dto.setBookAuthor(book.getAuthors() != null && !book.getAuthors().isEmpty() ?
                        String.join(", ", book.getAuthors()) : "Unknown Author");

                // ✅ UPDATED: Just pass the fileName, let frontend handle the image retrieval
                if (book.getBookImage() != null && !book.getBookImage().trim().isEmpty()) {
                    dto.setBookCover(book.getBookImage()); // Pass fileName directly
                } else {
                    dto.setBookCover(null); // No image available
                }
            } else {
                // Fallback
                dto.setBookId(bookId);
                dto.setBookTitle("Book " + bookId);
                dto.setBookAuthor("Unknown Author");
                dto.setBookCover(null);
            }
        } catch (Exception e) {
            System.err.println("Error getting book details: " + e.getMessage());
            // Fallback
            dto.setBookId(bookId);
            dto.setBookTitle("Book " + bookId);
            dto.setBookAuthor("Unknown Author");
            dto.setBookCover(null);
        }
    }
    //UPDATED: Get real user details from Users table using your existing repo
    private void setRealUserDetails(UserTransactionDto.TransactionResponseDto dto, Transaction transaction) {
        try {
            if (transaction.getSellerId() != null) {
                dto.setSeller(getUserDetails(transaction.getSellerId()));
            }
            if (transaction.getLenderId() != null) {
                dto.setLender(getUserDetails(transaction.getLenderId()));
            }
            if (transaction.getExchangerId() != null) {
                dto.setExchanger(getUserDetails(transaction.getExchangerId()));
            }
            if (transaction.getBorrowerId() != null) {
                dto.setBorrower(getUserDetails(transaction.getBorrowerId()));
            }
        } catch (Exception e) {
            System.err.println("Error getting user details: " + e.getMessage());
        }
    }

    private UserTransactionDto.UserDetailsDto getUserDetails(Long userId) {
        try {
            Optional<Users> userOpt = usersRepo.findById(userId);
            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                UserTransactionDto.UserDetailsDto userDto = new UserTransactionDto.UserDetailsDto();
                userDto.setUserId(userId);
                userDto.setName(user.getName());
                userDto.setEmail(user.getEmail());
                userDto.setPhone(String.valueOf(user.getPhone()));
                userDto.setLocation(user.getAddress() + (user.getCity() != null ? ", " + user.getCity() : ""));
                return userDto;
            }
        } catch (Exception e) {
            System.err.println("Error getting user " + userId + ": " + e.getMessage());
        }

        // Fallback
        UserTransactionDto.UserDetailsDto userDto = new UserTransactionDto.UserDetailsDto();
        userDto.setUserId(userId);
        userDto.setName("User " + userId);
        userDto.setEmail("user" + userId + "@example.com");
        userDto.setPhone("+94 77 123 4567");
        userDto.setLocation("Unknown");
        return userDto;
    }

    private UserTransactionDto.TrackingDto createTrackingEntry(String status, String timestamp, String description) {
        UserTransactionDto.TrackingDto tracking = new UserTransactionDto.TrackingDto();
        tracking.setStatus(status);
        tracking.setTimestamp(timestamp);
        tracking.setDescription(description);
        return tracking;
    }

    // UPDATED: Type mapping according to your requirements
    private String mapTransactionType(Transaction.TransactionType type) {
        switch (type) {
            case SALE: return "purchase";      // purchasedBooks
            case LEND: return "borrow";        // borrowedBooks
            case BIDDING: return "bidding";    // wonAuctions
            case EXCHANGE: return "exchange";  // exchangedBooks
            default: return type.name().toLowerCase();
        }
    }

    // FIXED: Status mapping to use correct enum values
    private String mapTransactionStatus(Transaction.TransactionStatus status) {
        switch (status) {
            case COMPLETED: return "delivered";
            case PENDING: return "pending";
            case CANCELLED: return "cancelled";
            case OVERDUE: return "overdue";
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
        if (transaction.getStatus() == Transaction.TransactionStatus.PENDING) {
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

    // FIXED: In-memory filtering with proper enum mapping
    private Page<Transaction> filterInMemory(Page<Transaction> transactions, UserTransactionDto.TransactionFilterDto filter) {
        List<Transaction> filtered = transactions.getContent().stream()
                .filter(t -> {
                    try {
                        // Status filter - FIXED
                        if (filter.getStatus() != null) {
                            Transaction.TransactionStatus targetStatus = mapFrontendStatusToBackend(filter.getStatus());
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
                        return false;
                    }
                })
                .collect(Collectors.toList());

        return new org.springframework.data.domain.PageImpl<>(
                filtered,
                transactions.getPageable(),
                filtered.size()
        );
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

    // Enhanced book image fallback
    private String generateFallbackBookImage(Long bookId) {
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