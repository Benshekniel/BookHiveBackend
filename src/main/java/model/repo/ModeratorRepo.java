package model.repo;

import jakarta.transaction.Transactional;
import model.entity.BookStore;
import model.entity.Donation;
import model.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface ModeratorRepo extends JpaRepository<Moderator, Long> {

    Optional<Moderator> findOneByEmailAndPassword(String email, String password);

    Moderator findByEmail(String email);

    @Query("SELECT a.name AS name, a.email AS email, a.role AS role, " +
            "u.fname AS fname, u.lname AS lname, u.phone AS phone, u.dob AS dob, u.idType AS idType, " +
            "u.idFront AS idFront, u.idBack AS idBack, u.gender AS gender, u.address AS address, " +
            "u.city AS city, u.state AS state, u.zip AS zip, u.billImage AS billImage, u.createdAt AS createdAt " +
            "FROM AllUsers a LEFT JOIN Users u ON a.email = u.email WHERE a.status = 'pending' AND a.role = 'user'")
    List<Map<String, Object>> findAllPending();

    @Query("SELECT a.name AS name, a.email AS email, a.role AS role, " +
            "u.fname AS fname, u.lname AS lname, u.phone AS phone, u.dob AS dob, u.idType AS idType, " +
            "u.idFront AS idFront, u.idBack AS idBack, u.gender AS gender, u.address AS address, " +
            "u.city AS city, u.state AS state, u.zip AS zip, u.billImage AS billImage, u.createdAt AS createdAt, " +
            "a.status AS status " +
            "FROM AllUsers a LEFT JOIN Users u ON a.email = u.email " +
            "WHERE a.status = model.entity.AllUsers.Status.active AND a.role = 'user'")
    List<Map<String, Object>> findAllActive();

    @Query("SELECT a.name AS name, a.email AS email, a.role AS role, " +
            "u.fname AS fname, u.lname AS lname, u.phone AS phone, u.dob AS dob, u.idType AS idType, " +
            "u.idFront AS idFront, u.idBack AS idBack, u.gender AS gender, u.address AS address, " +
            "u.city AS city, u.state AS state, u.zip AS zip, u.billImage AS billImage, u.createdAt AS createdAt, " +
            "a.status AS status " +
            "FROM AllUsers a LEFT JOIN Users u ON a.email = u.email " +
            "WHERE a.status IN (model.entity.AllUsers.Status.banned, model.entity.AllUsers.Status.disabled) AND a.role = 'user'")
    List<Map<String, Object>> findAllFlagged();

    @Modifying
    @Transactional
    @Query("UPDATE AllUsers a SET a.status = 'active' WHERE a.email = :email AND a.role = 'user'")
    int approveUserByEmail(@Param("email") String email);


    @Modifying
    @Transactional
    @Query("UPDATE AllUsers a SET a.status = 'rejected' WHERE a.email = :email AND a.role = 'user'")
    int rejectUserByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE AllUsers a SET a.status = 'banned' WHERE a.email = :email AND a.role = 'user'")
    int banUserByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE AllUsers a SET a.status = 'disabled' WHERE a.email = :email AND a.role = 'user'")
    int disableUserByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE AllUsers a SET a.status = 'active' WHERE a.email = :email AND a.role = 'user'")
    int activeUserByEmail(@Param("email") String email);

    // Count all active users
    @Query("SELECT COUNT(a) FROM AllUsers a WHERE a.status = 'active' AND a.role = 'user'")
    int countActiveUsers();

    // Count all flagged users (banned or disabled)
    @Query("SELECT COUNT(a) FROM AllUsers a WHERE (a.status = 'banned' OR a.status = 'disabled') AND a.role = 'user'")
    int countFlaggedUsers();

    // ✅ Fetch all donations that are PENDING or have NULL status
    @Query("SELECT d FROM Donation d WHERE d.status = 'PENDING' OR d.status IS NULL")
    List<Donation> getPendingDonations();

    @Modifying
    @Transactional
    @Query("UPDATE Donation d SET d.status = :status WHERE d.id = :donationId")
    int updateDonationStatus(@Param("donationId") Long donationId, @Param("status") String status);

    // ✅ Update status to REJECTED with reason
    @Modifying
    @Transactional
    @Query("UPDATE Donation d SET d.status = 'REJECTED', d.rejectedReason = :reason WHERE d.id = :donationId")
    int rejectDonation(@Param("donationId") Long donationId, @Param("reason") String reason);

    // ✅ Fetch all APPROVED donations
    @Query("SELECT d FROM Donation d WHERE d.status = 'APPROVED'")
    List<Donation> findAllApprovedDonations();

    // ✅ Fetch all REJECTED donations
    @Query("SELECT d FROM Donation d WHERE d.status = 'REJECTED'")
    List<Donation> findAllRejectedDonations();

    // ✅ Fetch all PENDING donations
    @Query("SELECT d FROM Donation d WHERE d.status = 'PENDING' OR d.status IS NULL")
    List<Donation> findAllPendingDonations();

    // ✅ Optional: Fetch donations by priority (e.g., High / Medium / Low)
//    @Query("SELECT d FROM Donation d WHERE d.priority = :priority")
//    List<Donation> findDonationsByPriority(String priority);


    @Query("SELECT b FROM BookStore b WHERE b.isApproved = 'PENDING' OR b.isApproved IS NULL")
    List<BookStore> findPendingOrUnapprovedBookStores();

    @Query("SELECT b FROM BookStore b WHERE b.isApproved = 'APPROVED' OR b.isApproved IS NULL")
    List<BookStore> findApprovedBookStores();

    @Query("SELECT b FROM BookStore b WHERE b.isApproved = 'REJECTED' OR b.isApproved IS NULL")
    List<BookStore> findRejectedBookStores();

    @Modifying
    @Transactional
    @Query("UPDATE BookStore b SET b.isApproved = 'REJECTED' WHERE b.user_id = :userId")
    int rejectBookStoreByUserId(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query("UPDATE BookStore b SET b.isApproved = 'APPROVED' WHERE b.user_id = :userId")
    int approveBookStoreByUserId(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query("UPDATE BookStore b SET b.isApproved = 'BANNED' WHERE b.user_id = :userId")
    int banBookStoreByUserId(@Param("userId") Integer userId);

//    @Modifying
//    @Transactional
//    @Query("UPDATE AllUsers a SET a.status = 'disabled' WHERE a.email = :email AND a.role = 'bookstore'")
//    int disableBookStoreByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE AllUsers a SET a.status = 'active' WHERE a.user_id = :user_id AND a.role = 'bookstore'")
    int activeBookStoreById(@Param("user_id") Integer user_id);

    @Modifying
    @Transactional
    @Query("UPDATE AllUsers a SET a.status = 'banned' WHERE a.user_id = :user_id AND a.role = 'bookstore'")
    int banBookStoreById(@Param("user_id") Integer user_id);

    @Modifying
    @Transactional
    @Query("UPDATE AllUsers a SET a.status = 'rejected' WHERE a.user_id = :user_id AND a.role = 'bookstore'")
    int rejectBookStoreById(@Param("user_id") Integer user_id);

}
