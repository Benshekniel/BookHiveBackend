package service.User.impl;


import jakarta.transaction.Transactional;
import model.dto.Bidding.UserBorrowRequestDTO;
import model.entity.AllUsers;
import model.entity.Bid.UserBorrowRequest;
import model.entity.UserBooks;
import model.repo.AllUsersRepo;
import model.repo.UserBooksRepo;
import model.repo.bid.BorrowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.User.BorrowService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowServiceImpl implements BorrowService {


    @Autowired
    private AllUsersRepo allUsersRepo;

    @Autowired
    private UserBooksRepo userBooksRepo;

    @Autowired
    private BorrowRepo borrowRequestRepo;

    @Override
    @Transactional
    public UserBorrowRequestDTO createBorrowRequest(UserBorrowRequestDTO incoming) {
        if (incoming == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        if (incoming.getBookId() == null) {
            throw new IllegalArgumentException("bookId is required");
        }
        if (incoming.getOwnerEmail() == null || incoming.getOwnerEmail().isBlank()) {
            throw new IllegalArgumentException("ownerEmail is required");
        }

        // 1) Fetch book details by bookId
        UserBooks book = userBooksRepo.findById(incoming.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found for id: " + incoming.getBookId()));

        String bookName = book.getTitle();
        String bookImageName = book.getBookImage();
        String author = (book.getAuthors() != null && !book.getAuthors().isEmpty())
                ? String.join(", ", book.getAuthors())
                : "Unknown";
        double lendPrice = book.getLendingAmount() != null ? book.getLendingAmount() : 0.0;

        // 2) Fetch owner details by ownerEmail from All_Users
        AllUsers owner = allUsersRepo.findByEmail(incoming.getOwnerEmail());
        if (owner == null) {
            throw new IllegalArgumentException("Owner not found for email: " + incoming.getOwnerEmail());
        }
        int ownerId = owner.getUser_id();
        String ownerName = owner.getName();

        // Optional: ensure the ownerEmail matches the book's userEmail
        // if (book.getUserEmail() != null && !book.getUserEmail().equalsIgnoreCase(incoming.getOwnerEmail())) {
        //     throw new IllegalArgumentException("ownerEmail does not match book owner's email");
        // }

        // 3) Build entity to save
        UserBorrowRequest entity = new UserBorrowRequest(
                incoming.getName(),                       // requestor name (from client)
                incoming.getUserId(),                     // requestor id (from client)
                incoming.getOwnerEmail(),                 // owner email (from client)
                incoming.getBookId(),                     // book id (from client)
                bookName,                                 // derived
                bookImageName,                            // derived
                author,                                   // derived
                lendPrice,                                // derived
                ownerName,                                // derived
                ownerId,                                  // derived
                incoming.getStatus() != null ? incoming.getStatus() : "PENDING", // default PENDING
                incoming.getDeliveryPrice(),              // from client
                incoming.getHandlingPrice()               // from client
        );

        UserBorrowRequest saved = borrowRequestRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    public UserBorrowRequestDTO getById(Long requestId) {
        UserBorrowRequest e = borrowRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow request not found: " + requestId));
        return toDTO(e);
    }

    @Override
    public List<UserBorrowRequestDTO> getByRequestor(int userId) {
        return borrowRequestRepo.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<UserBorrowRequestDTO> getByOwner(int ownerId) {
        return borrowRequestRepo.findByOwnerIdOrderByCreatedAtDesc(ownerId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserBorrowRequestDTO updateStatus(Long requestId, String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }
        UserBorrowRequest e = borrowRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow request not found: " + requestId));

        e.setStatus(status.trim().toUpperCase()); // PENDING, APPROVED, REJECTED, COMPLETED, CANCELLED
        UserBorrowRequest updated = borrowRequestRepo.save(e);
        return toDTO(updated);
    }

    // Mapper
    private UserBorrowRequestDTO toDTO(UserBorrowRequest e) {
        if (e == null) return null;
        UserBorrowRequestDTO dto = new UserBorrowRequestDTO();
        dto.setUserId(e.getUserId());
        dto.setName(e.getName());
        dto.setBookId(e.getBookId());
        dto.setOwnerEmail(e.getOwnerEmail());
        dto.setDeliveryPrice(e.getDeliveryPrice());
        dto.setHandlingPrice(e.getHandlingPrice());

        // derived/backfilled fields
        dto.setBookName(e.getBookName());
        dto.setBookImageName(e.getBookImageName());
        dto.setAuthor(e.getAuthor());
        dto.setLendPrice(e.getLendPrice());
        dto.setOwnerId(e.getOwnerId());
        dto.setOwnerName(e.getOwnerName());
        dto.setStatus(e.getStatus());

        return dto;
    }


    @Override
    public boolean existsBorrowRequest(int userId, Long bookId) {
        return borrowRequestRepo.existsByUserIdAndBookId(userId, bookId);
    }

    @Override
    public boolean existsActiveBorrowRequest(int userId, Long bookId) {
        return borrowRequestRepo.existsByUserIdAndBookIdAndStatusIn(
                userId,
                bookId,
                Arrays.asList("PENDING", "APPROVED", "PAYMENT_PENDING")
        );
    }

    @Override
    public List<UserBorrowRequest> getRequestsByUserEntity(int userId) {
        return borrowRequestRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<UserBorrowRequest> getRequestsByOwnerEntity(int ownerId) {
        return borrowRequestRepo.findByOwnerIdOrderByCreatedAtDesc(ownerId);
    }
/// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    @Transactional
    public UserBorrowRequest approve(Long requestId, Long bookId, boolean autoRejectOthers) {
        UserBorrowRequest existing = borrowRequestRepo.findByRequestIdAndBookId(requestId, bookId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow request not found for given requestId and bookId"));

        // Approve the target request
        int u = borrowRequestRepo.updateStatusByRequestAndBook(requestId, bookId, "APPROVED");
        if (u == 0) {
            throw new IllegalStateException("Failed to approve request");
        }

        // Auto-reject others for this book
        if (autoRejectOthers) {
            borrowRequestRepo.rejectOthersForBookExcept(bookId, requestId);
        }

        return borrowRequestRepo.findByRequestIdAndBookId(requestId, bookId)
                .orElseThrow(() -> new IllegalStateException("Approved request not found after update"));
    }

    @Override
    @Transactional
    public UserBorrowRequest reject(Long requestId, Long bookId) {
        borrowRequestRepo.findByRequestIdAndBookId(requestId, bookId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow request not found for given requestId and bookId"));

        int u = borrowRequestRepo.updateStatusByRequestAndBook(requestId, bookId, "REJECTED");
        if (u == 0) {
            throw new IllegalStateException("Failed to reject request");
        }

        return borrowRequestRepo.findByRequestIdAndBookId(requestId, bookId)
                .orElseThrow(() -> new IllegalStateException("Rejected request not found after update"));
    }

    @Override
    @Transactional
    public void delete(Long requestId, Long bookId) {
        UserBorrowRequest existing = borrowRequestRepo.findByRequestIdAndBookId(requestId, bookId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow request not found for given requestId and bookId"));

        int deleted = borrowRequestRepo.deleteByRequestIdAndBookId(requestId, bookId);
        if (deleted == 0) {
            throw new IllegalStateException("Failed to delete request");
        }
    }
}
