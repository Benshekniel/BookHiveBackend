package model.repo.BookStore;

import model.entity.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface BSTransactionRepo extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

}

