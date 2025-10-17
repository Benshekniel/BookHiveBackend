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


}

