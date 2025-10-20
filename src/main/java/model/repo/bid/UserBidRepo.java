package model.repo.bid;


import model.entity.Bid.User_Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface UserBidRepo extends JpaRepository<User_Bid, Integer> {

    // Fetch all bids placed for a specific book
    List<User_Bid> findByBookId(Long bookId);
}
