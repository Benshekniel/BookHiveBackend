// OrganizationRepository.java
package model.repo.organization;

import model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    
    // Basic finder methods
    Optional<Organization> findByUserId(Long userId);
    Optional<Organization> findByEmail(String email);
    List<Organization> findByNameContainingIgnoreCase(String name);
    boolean existsByEmail(String email);
    boolean existsByUserId(Long userId);
    
    // Complex queries with details
    @Query("SELECT o, u.name, u.email, u.role, " +
           "(SELECT COUNT(b) FROM BookRequest b WHERE b.organizationId = o.id), " +
           "(SELECT COUNT(d) FROM Donation d WHERE d.organizationId = o.id) " +
           "FROM Organization o " +
           "LEFT JOIN AllUsers u ON o.userId = u.user_id")
    List<Object[]> findAllOrganizationsWithDetails();
    
    @Query("SELECT o, u.name, u.email, u.role, " +
           "(SELECT COUNT(b) FROM BookRequest b WHERE b.organizationId = o.id), " +
           "(SELECT COUNT(d) FROM Donation d WHERE d.organizationId = o.id) " +
           "FROM Organization o " +
           "LEFT JOIN AllUsers u ON o.userId = u.user_id " +
           "WHERE o.id = :orgId")
    Optional<Object[]> findOrganizationWithDetails(@Param("orgId") Long orgId);
    
    @Query("SELECT o FROM Organization o WHERE o.twoFactorEnabled = true")
    List<Organization> findOrganizationsWithTwoFactorEnabled();
    
    @Query("SELECT o FROM Organization o ORDER BY " +
           "(SELECT COUNT(b) FROM BookRequest b WHERE b.organizationId = o.id) DESC")
    List<Organization> findOrganizationsOrderByBookRequestCountDesc();
    
    @Query("SELECT o FROM Organization o ORDER BY " +
           "(SELECT COUNT(d) FROM Donation d WHERE d.organizationId = o.id) DESC")
    List<Organization> findOrganizationsOrderByDonationCountDesc();
}