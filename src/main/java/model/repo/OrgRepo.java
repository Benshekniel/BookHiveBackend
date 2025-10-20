package model.repo;

import jakarta.transaction.Transactional;
import model.entity.Moderator;
import model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface OrgRepo extends JpaRepository<Organization, Long> {

    Optional<Organization> findOneByEmailAndPassword(String email, String password);

    Optional<Organization> findByRegNo(String regNo);

    Optional<Organization> findByEmail(String email);

    // Find organization by orgId
    Optional<Organization> findByOrgId(Long orgId);

    // Check if organization exists by orgId
    boolean existsByOrgId(Long orgId);

    // ✅ Get user_id from all_users by email
    @Query("SELECT a.user_id FROM AllUsers a WHERE a.email = :email")
    Integer findUserIdByEmailinAllUsers(@Param("email") String email);

    // ✅ Update user_id in bookstore where email matches
    @Modifying
    @Transactional
    @Query(value = "UPDATE organizations SET org_id = :userId WHERE email = :email", nativeQuery = true)
    int updateUserIdByEmail(@Param("userId") Integer userId, @Param("email") String email);

}

