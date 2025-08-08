package model.repo;

import model.entity.BookRequest;
import model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {

    List<BookRequest> findByOrganization(Organization organization);

    List<BookRequest> findByOrganizationAndStatus(Organization organization, BookRequest.RequestStatus status);

    Optional<BookRequest> findByRequestIdAndOrganization(Long requestId, Organization organization);

    @Query("SELECT COUNT(br) FROM BookRequest br WHERE br.organization = :org AND br.status = :status")
    long countByOrganizationAndStatus(@Param("org") Organization organization, @Param("status") BookRequest.RequestStatus status);

    @Query("SELECT COUNT(br) FROM BookRequest br WHERE br.organization = :org")
    long countByOrganization(@Param("org") Organization organization);

    List<BookRequest> findByOrganizationOrderByDateRequestedDesc(Organization organization);

    @Query("SELECT br FROM BookRequest br WHERE br.organization = :org AND " +
           "(:status = 'all' OR br.status = :status) " +
           "ORDER BY br.dateRequested DESC")
    List<BookRequest> findByOrganizationAndStatusFilterOrderByDateRequestedDesc(
            @Param("org") Organization organization, 
            @Param("status") String status);
}
