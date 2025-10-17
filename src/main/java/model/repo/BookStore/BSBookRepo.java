package model.repo.BookStore;

import model.entity.BSBook;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface BSBookRepo extends JpaRepository<BSBook, Integer> {

    List<BSBook> findByBookStore_StoreId(Integer storeId);

    Optional<BSBook> findByBookId(Integer bookId);

    List<BSBook> findByBookStore_StoreIdAndIsForSellingTrue(Integer storeId);
    List<BSBook> findByBookStore_StoreIdAndIsForSellingFalse(Integer storeId);
}

