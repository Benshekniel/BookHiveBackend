package model.repo.User;

import model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileSettingRepository extends JpaRepository<Users, Long> {

    /**
     * Find user by email
     */
    Optional<Users> findByEmail(String email);

    /**
     * Check if email exists excluding a specific user ID
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Users u WHERE u.email = :email AND u.userId != :userId")
    boolean existsByEmailAndUserIdNot(@Param("email") String email, @Param("userId") Long userId);

    /**
     * Find user by email and verify it's not a different user
     */
    @Query("SELECT u FROM Users u WHERE u.email = :email AND u.userId != :userId")
    Optional<Users> findByEmailAndUserIdNot(@Param("email") String email, @Param("userId") Long userId);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
}