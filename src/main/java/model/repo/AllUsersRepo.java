package model.repo;

import model.entity.AllUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface AllUsersRepo extends JpaRepository<AllUsers, Integer> {

    Optional<AllUsers> findOneByEmailAndPassword(String email, String password);

    AllUsers findByEmail(String email);

    // Additional methods for application approval process
    List<AllUsers> findByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);

    // New methods for messaging system - Role-based contact filtering
    List<AllUsers> findByRole(String role);

    List<AllUsers> findByStatus(AllUsers.Status status);

    List<AllUsers> findByRoleAndStatus(String role, AllUsers.Status status);

    List<AllUsers> findByRoleAndStatusNot(String role, AllUsers.Status status);

    @Query("SELECT u FROM AllUsers u WHERE u.role IN :roles AND u.status != :status")
    List<AllUsers> findByRoleInAndStatusNot(@Param("roles") List<String> roles, @Param("status") AllUsers.Status status);

    long countByRole(String role);

    long countByStatus(AllUsers.Status status);

    // Additional queries for messaging contacts
    @Query("SELECT u FROM AllUsers u WHERE u.role IN :roles AND u.user_id != :excludeUserId AND u.status != 'banned'")
    List<AllUsers> findContactsByRolesExcludingUser(@Param("roles") List<String> roles, @Param("excludeUserId") int excludeUserId);

    @Query("SELECT u FROM AllUsers u WHERE u.role = :role AND u.user_id != :excludeUserId AND u.status != 'banned'")
    List<AllUsers> findByRoleExcludingUser(@Param("role") String role, @Param("excludeUserId") int excludeUserId);

    // Find all active users (not banned/disabled) for messaging
    @Query("SELECT u FROM AllUsers u WHERE u.status IN ('active', 'pending') AND u.user_id != :excludeUserId")
    List<AllUsers> findAllActiveUsersExcluding(@Param("excludeUserId") int excludeUserId);
}