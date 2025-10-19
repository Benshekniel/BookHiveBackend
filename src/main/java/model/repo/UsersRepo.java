package model.repo;

import jakarta.transaction.Transactional;
import model.entity.Organization;
import model.entity.Transaction;
import model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {

    Optional<Users> findOneByEmailAndPassword(String email, String password);

    Optional<Users> findByEmail(String email);

    // Custom query for logged-in user data excluding password
    @Query("SELECT u FROM Users u WHERE u.email = :email")
    @Transactional
    Optional<Users> loginedUser(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET t.paymentAmount = :paymentAmount, " +
            "t.paMethodNew = :paMethodNew, t.paymentStatus = :paymentStatus " +
            "WHERE t.transactionId = :transactionId AND t.userId = :userId AND t.bookId = :bookId")
    int updatePayment(Long transactionId, Long userId, Long bookId, BigDecimal paymentAmount,
                      String paMethodNew, Transaction.PaymentStatus paymentStatus);

    @Query("SELECT u.address FROM Users u JOIN AllUsers au ON u.email = au.email WHERE au.user_id = :allUsersId")
    Optional<String> findAddressByAllUsersId(int allUsersId);

//    @Modifying
//    @Transactional
//    @Query("UPDATE Transaction t SET t.deliveryAddress = :address WHERE t.userId = :userId")
//    int updateDeliveryAddressByUserId(Long userId, String address);

    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET t.deliveryAddress = :address WHERE t.userId = :userId")
    int updateAllDeliveryAddressesByUserId(@Param("userId") Long userId, @Param("address") String address);

}
