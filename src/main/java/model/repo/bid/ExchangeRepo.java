package model.repo.bid;

import jakarta.transaction.Transactional;
import model.entity.AllUsers;
import model.entity.Bid.Bid_History;
import model.entity.Bid.Exchange_Books;
import model.entity.UserBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface ExchangeRepo extends JpaRepository<Exchange_Books, Integer> {

    @Query("SELECT a.user_id FROM AllUsers a WHERE a.email = :email")
    Integer findApproverIdByApproverEmail(@Param("email") String email);

    // ✅ New: get all books by user email
    @Query("SELECT b FROM UserBooks b WHERE b.userEmail = :email")
    List<UserBooks> findAllBooksByUserEmail(@Param("email") String email);

    // ✅ New: check if a user already has an exchange entry for a given book
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Exchange_Books e WHERE e.userId = :userId AND e.approverBookId = :bookId")
    boolean existsByUserIdAndBookId(@Param("userId") int userId, @Param("bookId") Long bookId);

    @Query("SELECT e FROM Exchange_Books e WHERE e.userId = :userId")
    List<Exchange_Books> findAllOutgoingExchangesByUserId(@Param("userId") int userId);

    @Query("SELECT e FROM Exchange_Books e WHERE e.approverId = :userId")
    List<Exchange_Books> findAllIncomingExchangesByApproverId(@Param("userId") int userId);


    @Query("SELECT b FROM UserBooks b WHERE b.bookId = :bookId")
    UserBooks findBookByBookId(@Param("bookId") Long bookId);


    @Modifying
    @Transactional
    @Query("UPDATE Exchange_Books e SET e.status = 'APPROVED', e.approverDeliveryPrice = :deliveryFee, e.approverHandlingPrice = :handlingFee WHERE e.exchangeId = :exchangeId")
    int approveExchange(@Param("exchangeId") Integer exchangeId,
                        @Param("deliveryFee") double deliveryFee,
                        @Param("handlingFee") double handlingFee);


    @Modifying
    @Transactional
    @Query("UPDATE Exchange_Books e SET e.status = 'REJECTED', e.rejectReason = :reason WHERE e.exchangeId = :exchangeId")
    int rejectExchange(@Param("exchangeId") Integer exchangeId,
                       @Param("reason") String reason);

    // ✅ Step 1: Get email from AllUsers by user_id
    @Query("SELECT a.email FROM AllUsers a WHERE a.user_id = :userId")
    String findEmailByUserId(@Param("userId") int userId);

    // ✅ Find address using email from Users table
    @Query("SELECT u.address FROM Users u WHERE u.email = :email")
    String findAddressByEmail(@Param("email") String email);



}
