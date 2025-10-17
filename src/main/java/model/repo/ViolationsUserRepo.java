package model.repo;

import jakarta.transaction.Transactional;
import model.entity.ViolationsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface ViolationsUserRepo extends JpaRepository<ViolationsUser, String> {

    // Custom query to insert a new ViolationsUser
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO violations_user (email, reason) VALUES (:email, :reason)", nativeQuery = true)
    void insertViolation(@Param("email") String email, @Param("reason") String reason);

    // Delete a violation by email
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM violations_user WHERE email = :email", nativeQuery = true)
    void deleteByEmail(@Param("email") String email);

    // ✅ Fetch the reason for violation by email
    @Query(value = "SELECT reason FROM violations_user WHERE email = :email", nativeQuery = true)
    String findReasonByEmail(@Param("email") String email);
}
