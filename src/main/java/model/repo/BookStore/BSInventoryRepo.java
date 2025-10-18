package model.repo.BookStore;

import model.entity.BSInventory;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface BSInventoryRepo extends JpaRepository<BSInventory, Integer> {

    Optional<BSInventory> findByInventoryId(Integer inventoryId);

    List<BSInventory> findByIsForDonationFalse ();

    List<BSInventory> findByBookStore_StoreIdAndIsForDonationFalse(Integer storeId);
    List<BSInventory> findByBookStore_StoreIdAndIsForDonationTrue(Integer storeId);

    long countByBookStore_StoreId (Integer storeId);

    @Query("SELECT SUM(i.stockCount) FROM BSInventory i where i.bookStore.storeId = :storeId and i.isForDonation = false")
    long getTotalRegularStockByStoreId (Integer storeId);

    @Query("SELECT SUM(i.sellableCount) FROM BSInventory i where i.bookStore.storeId = :storeId and i.isForDonation = false")
    long getSellableRegularStockByStoreId (Integer storeId);

//    @Query("SELECT SUM(i.stockCount) FROM BSInventory i where i.bookStore.storeId = :storeId and i.condition = 'NEW' and i.isForDonation = false")
//    long getNewBooksStockCountByStoreId (Integer storeId);

    long countByBookStore_StoreIdAndIsForDonationFalseAndStockCountLessThan (Integer storeId, Integer stockThreshold);

    List<BSInventory> findTop3ByBookStore_StoreIdAndIsForDonationFalseOrderByStockCountDesc(Integer storeId);
    List<BSInventory> findTop3ByBookStore_StoreIdAndIsForDonationFalseOrderByStockCountAsc(Integer storeId);

}

//private long totalBooks;            // SUM of stockCount
//private long totalSellable;         // SUM of sellableCount
//private long newBooks;              // count with condition = NEW
//
//private long lowStockAlerts;         // count where stockCount < threshold
//private List<String> lowStockTitles; // top 3 low stock records' titles
//
//private List<String> topTitles;