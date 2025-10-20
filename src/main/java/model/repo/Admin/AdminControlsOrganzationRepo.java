package model.repo.Admin;

import jakarta.transaction.Transactional;
import model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface AdminControlsOrganzationRepo extends JpaRepository<Organization, Integer> {


    // ✅ Active organizations
    @Query("SELECT a.email FROM AllUsers a WHERE a.role = 'organization' AND a.status = 'active'")
    List<String> findActiveOrganizationEmails();

    // ✅ Banned organizations
    @Query("SELECT a.email FROM AllUsers a WHERE a.role = 'organization' AND a.status = 'banned'")
    List<String> findBannedOrganizationEmails();

    // ✅ Rejected organizations
    @Query("SELECT a.email FROM AllUsers a WHERE a.role = 'organization' AND a.status = 'rejected'")
    List<String> findRejectedOrganizationEmails();

    // ✅ Pending organizations
    @Query("SELECT a.email FROM AllUsers a WHERE a.role = 'organization' AND a.status = 'pending'")
    List<String> findPendingOrganizationEmails();



    // ✅ Find organization details by their emails
    @Query("SELECT o FROM Organization o WHERE o.email IN :emails")
    List<Organization> findOrganizationsByEmails(@Param("emails") List<String> emails);


    //Apply approve
    @Modifying
    @Transactional
    @Query("UPDATE AllUsers a SET a.status = 'active' WHERE a.user_id = :user_id AND a.role = 'organization'")
    int activateOrganizationById(@Param("user_id") Integer user_id);

    //Apply banned
    @Modifying
    @Transactional
    @Query("UPDATE AllUsers a SET a.status = 'banned' WHERE a.user_id = :user_id AND a.role = 'organization'")
    int banOrganizationById(@Param("user_id") Integer user_id);

    //Apply rejected
    @Modifying
    @Transactional
    @Query("UPDATE AllUsers a SET a.status = 'rejected' WHERE a.user_id = :user_id AND a.role = 'organization'")
    int rejectOrganizationById(@Param("user_id") Integer user_id);


}
