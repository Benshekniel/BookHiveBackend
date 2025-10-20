package model.repo.bid;


import jakarta.transaction.Transactional;
import model.dto.Bidding.UserBorrowRequestDTO;
import model.entity.Bid.UserBorrowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface BorrowRepo extends JpaRepository<UserBorrowRequest, Long> {

    // Requests created by a specific user (requestor)
    List<UserBorrowRequest> findByUserIdOrderByCreatedAtDesc(int userId);

    // Requests directed to a specific owner
    List<UserBorrowRequest> findByOwnerIdOrderByCreatedAtDesc(int ownerId);

    // Optional: check duplicates for same book by same user while pending
    List<UserBorrowRequest> findByBookIdAndUserIdAndStatus(Long bookId, int userId, String status);

    // Any request (any status)
    boolean existsByUserIdAndBookId(int userId, Long bookId);

    // Optional: only “active” requests (PENDING/APPROVED/PAYMENT_PENDING)
    boolean existsByUserIdAndBookIdAndStatusIn(int userId, Long bookId, Collection<String> statusList);


    @Query("select new model.dto.Bidding.UserBorrowRequestDTO(" +
            "u.userId, u.name, u.bookId, u.ownerEmail, u.deliveryPrice, u.handlingPrice) " +
            "from UserBorrowRequest u " +
            "where u.userId = :userId " +
            "order by u.createdAt desc")
    List<UserBorrowRequestDTO> findDtoByUserId(@Param("userId") int userId);

    @Query("select new model.dto.Bidding.UserBorrowRequestDTO(" +
            "u.userId, u.name, u.bookId, u.ownerEmail, u.deliveryPrice, u.handlingPrice) " +
            "from UserBorrowRequest u " +
            "where u.ownerId = :ownerId " +
            "order by u.createdAt desc")
    List<UserBorrowRequestDTO> findDtoByOwnerId(@Param("ownerId") int ownerId);



    Optional<UserBorrowRequest> findByRequestIdAndBookId(Long requestId, Long bookId);

    @Modifying
    @Transactional
    @Query("update UserBorrowRequest r set r.status = :status " +
            "where r.requestId = :requestId and r.bookId = :bookId")
    int updateStatusByRequestAndBook(@Param("requestId") Long requestId,
                                     @Param("bookId") Long bookId,
                                     @Param("status") String status);

    // Approve one -> reject others (only if they’re still actionable)
    @Modifying
    @Transactional
    @Query("update UserBorrowRequest r set r.status = 'REJECTED' " +
            "where r.bookId = :bookId and r.requestId <> :requestId " +
            "and upper(r.status) in ('PENDING','APPROVED','PAYMENT_PENDING')")
    int rejectOthersForBookExcept(@Param("bookId") Long bookId,
                                  @Param("requestId") Long requestId);

    // Delete by both keys (derived)
    @Transactional
    int deleteByRequestIdAndBookId(Long requestId, Long bookId);



}
