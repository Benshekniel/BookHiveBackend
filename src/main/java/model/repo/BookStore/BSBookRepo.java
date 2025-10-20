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

    long countByBookStore_StoreId (Integer storeId);

    long countByBookStore_StoreIdAndIsForSellingFalse(Integer storeId);
    long countByBookStore_StoreIdAndIsForSellingFalseAndStatusEquals(Integer storeId, BSBook.BookStatus status);

    long countByBookStore_StoreIdAndIsForSellingTrue(Integer storeId);
    long countByBookStore_StoreIdAndIsForSellingTrueAndStatusEquals(Integer storeId, BSBook.BookStatus status);

    @Query("SELECT AVG(b.lendFee) FROM BSBook b WHERE b.bookStore.storeId = :storeId AND b.lendFee IS NOT NULL")
    Double getAverageLendFee(@Param("storeId") Integer storeId);

    @Query("SELECT AVG(b.sellPrice) FROM BSBook b WHERE b.bookStore.storeId = :storeId AND b.sellPrice IS NOT NULL")
    Double getAverageSellPrice(@Param("storeId") Integer storeId);

    @Query("SELECT AVG(b.lendingPeriod) FROM BSBook b WHERE b.bookStore.storeId = :storeId AND b.lendingPeriod IS NOT NULL")
    Double getAverageLendingPeriod(@Param("storeId") Integer storeId);

}

