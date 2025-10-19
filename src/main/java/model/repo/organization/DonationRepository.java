//package model.repo.organization;
//
//import model.entity.Donation;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface DonationRepository extends JpaRepository<Donation, Long> {
//    List<Donation> findByOrganizationIdOrderByDateDonatedDesc(Long organizationId);
//
//    List<Donation> findByOrganizationIdAndStatusAndFeedbackIsNull(Long organizationId, String status);
//
//    Long countByOrganizationId(Long organizationId);
//
//    Long countByOrganizationIdAndStatus(Long organizationId, String status);
//
//    @Query(value = "SELECT COALESCE(SUM(d.quantity), 0) FROM donations d WHERE d.organization_id = :organizationId AND d.status = :status", nativeQuery = true)
//    long countBooksByOrganizationIdAndStatus(@Param("organizationId") Long organizationId, @Param("status") String status);
//
//    @Query(value = "SELECT COALESCE(SUM(d.quantity), 0) FROM donations d WHERE d.organization_id = :organizationId AND d.status = :status AND d.date_received BETWEEN :start AND :end", nativeQuery = true)
//    long countBooksByOrganizationIdAndStatusAndDateReceivedBetween(
//            @Param("organizationId") Long organizationId,
//            @Param("status") String status,
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end);
//
//    @Query(value = "SELECT COALESCE(COUNT(d.id), 0) FROM donations d WHERE d.organization_id = :organizationId AND d.date_donated BETWEEN :start AND :end", nativeQuery = true)
//    long countByOrganizationIdAndDateDonatedBetween(
//            @Param("organizationId") Long organizationId,
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end);
//}
////@Repository
////public interface DonationRepository extends JpaRepository<Donation, Long> {
////    List<Donation> findByOrganizationIdOrderByDateDonatedDesc(Long organizationId);
////
////    List<Donation> findByOrganizationIdAndStatusAndFeedbackIsNull(Long organizationId, String status);
////
////    Long countByOrganizationId(Long organizationId);
////
////    Long countByOrganizationIdAndStatus(Long organizationId, String status);
////
////    @Query("SELECT SUM(d.quantity) FROM Donation d WHERE d.organization.id = :organizationId AND d.status = :status")
////    Long countBooksByOrganizationIdAndStatus(@Param("organizationId") Long organizationId, @Param("status") String status);
////
////    @Query("SELECT SUM(d.quantity) FROM Donation d WHERE d.organization.id = :organizationId AND d.status = :status AND d.dateReceived BETWEEN :start AND :end")
////    Long countBooksByOrganizationIdAndStatusAndDateReceivedBetween(
////            @Param("organizationId") Long organizationId,
////            @Param("status") String status,
////            @Param("start") LocalDateTime start,
////            @Param("end") LocalDateTime end);
////
////    Long countByOrganizationIdAndDateDonatedBetween(
////            Long organizationId, LocalDateTime start, LocalDateTime end);
////}