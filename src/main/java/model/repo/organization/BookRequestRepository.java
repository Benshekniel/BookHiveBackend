package model.repo.organization;

import model.entity.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
    List<BookRequest> findByOrganizationOrgIdOrderByDateRequestedDesc(Long orgId);

    @Query("SELECT br FROM BookRequest br WHERE br.organization.orgId = :orgId ORDER BY br.dateRequested DESC LIMIT :limit")
    List<BookRequest> findRecentByOrganizationId(@Param("orgId") Long orgId, @Param("limit") int limit);

    Long countByOrganizationOrgId(Long orgId);

    Long countByOrganizationOrgIdAndStatus(Long orgId, String status);

    @Query("SELECT COALESCE(COUNT(br), 0) FROM BookRequest br WHERE br.organization.orgId = :orgId AND br.status = :status AND br.dateRequested BETWEEN :start AND :end")
    Long countByOrganizationOrgIdAndStatusAndDateRequestedBetween(
            @Param("orgId") Long orgId,
            @Param("status") String status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}

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
//    List<BookRequest> findByOrgIdOrderByDateRequestedDesc(Long orgId);
//
//    @Query("SELECT br FROM BookRequest br WHERE br.orgId = :orgId ORDER BY br.dateRequested DESC LIMIT :limit")
//    List<BookRequest> findRecentByOrgId(@Param("orgId") Long orgId, @Param("limit") int limit);
//
//    Long countByOrgId(Long orgId);
//
//    Long countByOrgIdAndStatus(Long orgId, String status);
//
//    @Query("SELECT COALESCE(COUNT(br), 0) FROM BookRequest br WHERE br.orgId = :orgId AND br.status = :status AND br.dateRequested BETWEEN :start AND :end")
//    Long countByOrgIdAndStatusAndDateRequestedBetween(
//            @Param("orgId") Long orgId,
//            @Param("status") String status,
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end);
//}