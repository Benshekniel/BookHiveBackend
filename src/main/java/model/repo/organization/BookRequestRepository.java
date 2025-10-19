//package model.repo.organization;
//
//import model.entity.BookRequest;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
//    List<BookRequest> findByOrganizationIdOrderByDateRequestedDesc(Long organizationId);
//
//    @Query("SELECT br FROM BookRequest br WHERE br.organization.id = :organizationId ORDER BY br.dateRequested DESC LIMIT :limit")
//    List<BookRequest> findRecentByOrganizationId(@Param("organizationId") Long organizationId, @Param("limit") int limit);
//
//    Long countByOrganizationId(Long organizationId);
//
//    Long countByOrganizationIdAndStatus(Long organizationId, String status);
//
//    Long countByOrganizationIdAndStatusAndDateRequestedBetween(
//            Long organizationId, String status, LocalDateTime start, LocalDateTime end);
//}