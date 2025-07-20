package model.repo.BookStore;

import model.entity.BookStore;
import model.entity.AllUsers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookStoreRepo extends JpaRepository<BookStore, Integer> {

//    // Find bookstore by user
//    Optional<BookStore> findByUser(AllUsers user);

    // Find bookstore by user ID
    @Query("SELECT bs FROM BookStore bs WHERE bs.user.user_id = :user_id")
    Optional<BookStore> findByUserId(@Param("user_id") Integer user_id);

    // Find bookstores by type
    List<BookStore> findByBooksType(BookStore.BookType booksType);

    // Search bookstores by store name
    List<BookStore> findByStoreNameContainingIgnoreCase(String storeName);

    // Check if user already has a store
    boolean existsByUser(AllUsers user);

//    // Check if user already has a store by user_id
//    @Query("SELECT COUNT(bs) > 0 FROM BookStore bs WHERE bs.user.user_id = :user_id")
//    boolean existsByUserId(@Param("user_id") Integer user_id);
//
//    // Search by location (from user's address)
//    @Query("SELECT bs FROM BookStore bs WHERE bs.user.address LIKE %:location%")
//    List<BookStore> findByLocationContaining(@Param("location") String location);
}