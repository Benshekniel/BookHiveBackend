package model.repo.Admin;

import model.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface moderatorRepository extends JpaRepository<Moderator, Long> {

    // ==================== READ OPERATIONS ====================

    // Find moderator by email
    Optional<Moderator> findByEmail(String email);

    // Find moderators by name (exact match)
    List<Moderator> findByName(String name);

    // Find moderators by name containing (case-insensitive)
    @Query("SELECT m FROM Moderator m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Moderator> findByNameContainingIgnoreCase(@Param("name") String name);

    // Search moderators by name or email (case-insensitive)
    @Query("SELECT m FROM Moderator m WHERE " +
            "LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(m.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Moderator> searchByNameOrEmail(@Param("searchTerm") String searchTerm);

    // Find moderators by city
    List<Moderator> findByCity(String city);

    // Find moderators by city (case-insensitive)
    @Query("SELECT m FROM Moderator m WHERE LOWER(m.city) = LOWER(:city)")
    List<Moderator> findByCityIgnoreCase(@Param("city") String city);

    // Find moderators by experience level
    List<Moderator> findByExperience(Integer experience);

    // Find moderators by experience greater than or equal
    @Query("SELECT m FROM Moderator m WHERE m.experience >= :minExperience")
    List<Moderator> findByExperienceGreaterThanEqual(@Param("minExperience") Integer minExperience);

    // Find moderators by experience range
    @Query("SELECT m FROM Moderator m WHERE m.experience BETWEEN :minExp AND :maxExp")
    List<Moderator> findByExperienceBetween(@Param("minExp") Integer minExperience,
                                            @Param("maxExp") Integer maxExperience);

    // Find moderators by date of birth
    List<Moderator> findByDob(LocalDate dob);

    // Find moderators born between dates
    @Query("SELECT m FROM Moderator m WHERE m.dob BETWEEN :startDate AND :endDate")
    List<Moderator> findByDobBetween(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

    // Find moderators born after a specific date
    @Query("SELECT m FROM Moderator m WHERE m.dob > :date")
    List<Moderator> findByDobAfter(@Param("date") LocalDate date);

    // Find moderators born before a specific date
    @Query("SELECT m FROM Moderator m WHERE m.dob < :date")
    List<Moderator> findByDobBefore(@Param("date") LocalDate date);

    // Find moderators by phone number
    List<Moderator> findByPhone(int phone);

    // Find moderators by address containing
    @Query("SELECT m FROM Moderator m WHERE LOWER(m.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<Moderator> findByAddressContainingIgnoreCase(@Param("address") String address);

    // Find all moderators ordered by ID (newest first)
    @Query(value = "SELECT * FROM moderator ORDER BY id DESC", nativeQuery = true)
    List<Moderator> findAllModeratorsOrderedByIdDesc();



    // Find all moderators ordered by name
    List<Moderator> findAllByOrderByNameAsc();

    // Find all moderators ordered by experience (highest first)
    List<Moderator> findAllByOrderByExperienceDesc();

    // Find all moderators ordered by date of birth (youngest first)
    List<Moderator> findAllByOrderByDobDesc();

    // Count moderators by city
    long countByCity(String city);

    // Count moderators by experience level
    long countByExperience(Integer experience);

    // Count moderators with experience greater than or equal
    @Query("SELECT COUNT(m) FROM Moderator m WHERE m.experience >= :minExperience")
    long countByExperienceGreaterThanEqual(@Param("minExperience") Integer minExperience);

    // Count moderators by age range (calculated from DOB)
    @Query("SELECT COUNT(m) FROM Moderator m WHERE m.dob BETWEEN :startDate AND :endDate")
    long countByAgeRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // ==================== VALIDATION OPERATIONS ====================

    // Check if email exists (for validation during creation/update)
    boolean existsByEmail(String email);

    // Check if email exists excluding current moderator (for update validation)
    boolean existsByEmailAndIdNot(String email, Long id);

    // Check if phone exists
    boolean existsByPhone(int phone);

    // Check if phone exists excluding current moderator
    boolean existsByPhoneAndIdNot(int phone, Long id);

    // ==================== ADVANCED READ OPERATIONS ====================

    // Find moderators with specific criteria using JPQL
    @Query("SELECT m FROM Moderator m WHERE " +
            "(:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR LOWER(m.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:city IS NULL OR LOWER(m.city) = LOWER(:city)) AND " +
            "(:minExperience IS NULL OR m.experience >= :minExperience) AND " +
            "(:maxExperience IS NULL OR m.experience <= :maxExperience)")
    List<Moderator> findWithFilters(@Param("name") String name,
                                    @Param("email") String email,
                                    @Param("city") String city,
                                    @Param("minExperience") Integer minExperience,
                                    @Param("maxExperience") Integer maxExperience);

    // Find top N moderators by experience
    @Query("SELECT m FROM Moderator m ORDER BY m.experience DESC")
    List<Moderator> findTopByExperience();

    // Find moderators with experience in specific range and from specific city
    @Query("SELECT m FROM Moderator m WHERE m.experience BETWEEN :minExp AND :maxExp AND LOWER(m.city) = LOWER(:city)")
    List<Moderator> findByExperienceRangeAndCity(@Param("minExp") Integer minExperience,
                                                 @Param("maxExp") Integer maxExperience,
                                                 @Param("city") String city);

    // Find unique cities where moderators are located
    @Query("SELECT DISTINCT m.city FROM Moderator m WHERE m.city IS NOT NULL ORDER BY m.city")
    List<String> findDistinctCities();

    // Find unique experience levels
    @Query("SELECT DISTINCT m.experience FROM Moderator m WHERE m.experience IS NOT NULL ORDER BY m.experience DESC")
    List<Integer> findDistinctExperienceLevels();

    // Get statistics - average experience
    @Query("SELECT AVG(m.experience) FROM Moderator m WHERE m.experience IS NOT NULL")
    Double findAverageExperience();

    // Get statistics - max experience
    @Query("SELECT MAX(m.experience) FROM Moderator m")
    Integer findMaxExperience();

    // Get statistics - min experience
    @Query("SELECT MIN(m.experience) FROM Moderator m")
    Integer findMinExperience();

    // ==================== DELETE OPERATIONS ====================

    // Delete moderator by email
    @Modifying
    @Transactional
    @Query("DELETE FROM Moderator m WHERE m.email = :email")
    int deleteByEmail(@Param("email") String email);

    // Delete moderators by city
    @Modifying
    @Transactional
    @Query("DELETE FROM Moderator m WHERE LOWER(m.city) = LOWER(:city)")
    int deleteByCity(@Param("city") String city);

    // Delete moderators by experience level
    @Modifying
    @Transactional
    @Query("DELETE FROM Moderator m WHERE m.experience = :experience")
    int deleteByExperience(@Param("experience") Integer experience);

    // Delete moderators with experience less than specified value
    @Modifying
    @Transactional
    @Query("DELETE FROM Moderator m WHERE m.experience < :minExperience")
    int deleteByExperienceLessThan(@Param("minExperience") Integer minExperience);

    // Delete moderators by phone number
    @Modifying
    @Transactional
    @Query("DELETE FROM Moderator m WHERE m.phone = :phone")
    int deleteByPhone(@Param("phone") int phone);

    // Delete moderators by IDs (batch delete)
    @Modifying
    @Transactional
    @Query("DELETE FROM Moderator m WHERE m.id IN :ids")
    int deleteByIdIn(@Param("ids") List<Long> ids);

    // Delete moderators born before a specific date
    @Modifying
    @Transactional
    @Query("DELETE FROM Moderator m WHERE m.dob < :date")
    int deleteByDobBefore(@Param("date") LocalDate date);

    // ==================== CUSTOM BULK OPERATIONS ====================

    // Update city for all moderators in a specific city
    @Modifying
    @Transactional
    @Query("UPDATE Moderator m SET m.city = :newCity WHERE LOWER(m.city) = LOWER(:oldCity)")
    int updateCityBulk(@Param("oldCity") String oldCity, @Param("newCity") String newCity);

    // Increment experience for all moderators in a city
    @Modifying
    @Transactional
    @Query("UPDATE Moderator m SET m.experience = m.experience + :increment WHERE LOWER(m.city) = LOWER(:city)")
    int incrementExperienceByCity(@Param("city") String city, @Param("increment") Integer increment);

    // Update address for moderators in a specific city
    @Modifying
    @Transactional
    @Query("UPDATE Moderator m SET m.address = CONCAT(m.address, ', ', :suffix) WHERE LOWER(m.city) = LOWER(:city)")
    int updateAddressByCityWithSuffix(@Param("city") String city, @Param("suffix") String suffix);
}