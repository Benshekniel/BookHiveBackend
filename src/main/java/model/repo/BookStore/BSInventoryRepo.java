package model.repo.BookStore;

import model.entity.BSInventory;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface BSInventoryRepo extends JpaRepository<BSInventory, Integer> {

    Optional<BSInventory> findByInventoryId(Integer inventoryId);

    List<BSInventory> findAllByIsForDonationFalse();

    List<BSInventory> findAllByBookStore_StoreIdAndIsForDonationFalse(Integer storeId);
    List<BSInventory> findAllByBookStore_StoreIdAndIsForDonationTrue(Integer storeId);

    long countByBookStore_StoreId (Integer storeId);

    List<BSInventory> findAllByCategory (String category);

    // REGULAR inventory stat functions:
    
    @Query("select SUM(i.stockCount) from BSInventory i where i.bookStore.storeId = :storeId and i.isForDonation = false")
    long getTotalRegularStockByStoreId (Integer storeId);

    @Query("select SUM(i.stockCount) from BSInventory i where i.bookStore.storeId = :storeId and i.condition = 'NEW' and i.isForDonation = false")
    long getTotalNewRegularStockByStoreId (Integer storeId);

    @Query("select SUM(i.sellableCount) from BSInventory i where i.bookStore.storeId = :storeId and i.isForDonation = false")
    long getSellableRegularStockByStoreId (Integer storeId);

    long countByBookStore_StoreIdAndIsForDonationFalseAndStockCountLessThan (Integer storeId, Integer stockThreshold);

    List<BSInventory> findTop3ByBookStore_StoreIdAndIsForDonationFalseOrderByStockCountDesc(Integer storeId);
    List<BSInventory> findTop3ByBookStore_StoreIdAndIsForDonationFalseOrderByStockCountAsc(Integer storeId);

    // DONATION inventory stat functions:

    @Query("select SUM(i.stockCount) from BSInventory i where i.bookStore.storeId = :storeId and i.isForDonation = true")
    long getTotalDonationStockByStoreId (Integer storeId);
    
}
