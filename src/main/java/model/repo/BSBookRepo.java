package model.repo;

import model.entity.BSBook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** For BookStore users' books. */
@Repository
public interface BSBookRepo extends JpaRepository<BSBook, Integer> {

    Optional<BSBook> findByBookId(Integer bookId);

    List<BSBook> findByBookStore_StoreId(Integer storeId);

    List<BSBook> findByBookStore_StoreIdAndListingTypeIn(Integer storeId, List<BSBook.ListingType> listTypes);

    long countBSBooksByBookStore_StoreId(Integer storeId);

    long countByBookStore_StoreIdAndListingTypeIn(Integer storeId, List<BSBook.ListingType> listTypes);

    long countByBookStore_StoreIdAndStatusAndListingTypeIn (
            Integer storeId, BSBook.BookStatus status, List<BSBook.ListingType> listTypes);


    Integer countByBookStore_StoreId(Integer storeId);

    Integer countByBookStore_StoreIdAndStatus(Integer storeId, BSBook.BookStatus status);


}
