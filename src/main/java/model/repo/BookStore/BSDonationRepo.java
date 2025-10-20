package model.repo.BookStore;

import model.entity.Donation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BSDonationRepo extends JpaRepository<Donation, Long> {

    /** pass "approved" as the status to get currently ongoing donation events */
    List<Donation> findAllByStatusEqualsIgnoreCase(String status);


}
