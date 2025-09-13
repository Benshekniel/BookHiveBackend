package model.repo;

import model.entity.BSBook;
import model.dto.BookStore.BSStatDTOs;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/** For BookStore users' books. */
@Repository
public interface BSBookRepo extends JpaRepository<BSBook, Integer> {

    Optional<BSBook> findByBookId(Integer bookId);

    List<BSBook> findByBookStore_StoreId(Integer storeId);

    List<BSBook> findByBookStore_StoreIdAndListingTypeInAndStatusNot(
            Integer storeId,
            List<BSBook.ListingType> listTypes,
            BSBook.BookStatus notStatus);

    /** Inventory, Donate  */
    List<BSBook> findByBookStore_StoreIdAndStatusAndListingType (
            Integer storeId,
            BSBook.BookStatus neededStatus,
            BSBook.ListingType listType);

    /** Inventory, Donate : for NOT donating books */
    List<BSBook> findByBookStore_StoreIdAndStatusAndListingTypeNot (
            Integer storeId,
            BSBook.BookStatus neededStatus,
            BSBook.ListingType listType);

    /** Available n Lent, LendOnly n Sell&Lend */
    List<BSBook> findByBookStore_StoreIdAndStatusInAndListingTypeIn (
            Integer storeId,
            List<BSBook.BookStatus> statuses,
            List<BSBook.ListingType> listTypes
    );

    @Query(value = """
    SELECT
        COUNT(*) AS totalBooks,
        COUNT(*) FILTER (WHERE status IN ('AVAILABLE','SOLD')) AS activeListings,
        COUNT(*) FILTER (WHERE status = 'INVENTORY') AS inInventory,
        COUNT(*) FILTER (WHERE status = 'SOLD') AS soldCount,
        COALESCE(AVG((pricing ->> 'sellPrice')::numeric), 0) AS avgSellPrice
    FROM bookstore_books
    WHERE store_id = :storeId
      AND listing_type IN ('SELL_ONLY', 'SELL_AND_LEND')
    """, nativeQuery = true)
    BSStatDTOs.SaleStatDTO getSaleStats(@Param("storeId") Integer storeId);

    @Query(value = """
    SELECT
        COUNT(*) AS totalBooks,
        COUNT(*) FILTER (WHERE status IN ('AVAILABLE','LENT')) AS activeListings,
        COUNT(*) FILTER (WHERE status = 'INVENTORY') AS inInventory,
        COUNT(*) FILTER (WHERE status = 'LENT') AS onLoanCount,
        COALESCE(AVG((pricing ->> 'lendPrice')::numeric), 0) AS avgLendPrice
    FROM bookstore_books
    WHERE store_id = :storeId
      AND listing_type IN ('LEND_ONLY', 'SELL_AND_LEND')
    """, nativeQuery = true)
    BSStatDTOs.LendStatDTO getLendStats(@Param("storeId") Integer storeId);

    @Query(value = """
    SELECT
        COUNT(*) FILTER ( WHERE status = 'INVENTORY' ) AS totalBooks,
        COUNT(*) FILTER (
            WHERE status = 'INVENTORY'
              AND listing_type IN ('SELL_ONLY','SELL_AND_LEND')
        ) AS sellInventory,
        COUNT(*) FILTER (
            WHERE status = 'INVENTORY'
              AND listing_type IN ('LEND_ONLY','SELL_AND_LEND')
        ) AS lendInventory,
        COUNT(*) FILTER (
            WHERE status = 'INVENTORY'
              AND listing_type = 'NOT_SET'
        ) AS notSetInventory
    FROM bookstore_books
    WHERE store_id = :storeId
    """, nativeQuery = true)
    BSStatDTOs.RegularInventoryStatDTO getRegularInventoryStats(@Param("storeId") Integer storeId);

    @Query(value = """
    SELECT
        COUNT(*) FILTER ( WHERE status = 'INVENTORY' ) AS totalBooks,
        COUNT(*) FILTER (
            WHERE status = 'DONATED'
              AND listing_type = 'DONATE'
        ) AS donated,
        COUNT(*) FILTER (
            WHERE status = 'INVENTORY'
              AND listing_type = 'DONATE'
        ) AS donationStock,
        COUNT(*) FILTER (
            WHERE status = 'DONATED'
              AND listing_type = 'DONATE'
        ) AS impactScore
    FROM bookstore_books
    WHERE store_id = :storeId
      AND listing_type = 'DONATE'
    """, nativeQuery = true)
    BSStatDTOs.DonationInventoryStatDTO getDonationInventoryStats(@Param("storeId") Integer storeId);

}
