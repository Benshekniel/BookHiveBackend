package model.repo.bid;

import jakarta.transaction.Transactional;
import model.entity.Bid.Bid_History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface BidHistoryRepo extends JpaRepository<Bid_History, Integer> {

    // ✅ Automatically generates: SELECT * FROM bid_history WHERE book_id = ?
    List<Bid_History> findByBookId(Long bookId);

    // ✅ Update bid_winner and mark bid_end = true
    @Transactional
    @Modifying
    @Query("UPDATE Bid_History b SET b.bidWinner = :winnerName, b.bidEnd = TRUE WHERE b.bid_id = :bidId")
    int updateBidWinnerAndEndBid(String winnerName, Long bidId);


//    // ✅ Update bid_winner by bidId
//    @Transactional
//    @Modifying
//    @Query("UPDATE Bid_History b SET b.bidWinner = :winnerName WHERE b.bid_id = :bidId")
//    int updateBidWinnerById(String winnerName, Long bidId);
}
