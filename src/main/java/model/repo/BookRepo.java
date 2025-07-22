package model.repo;

import model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {

    // Basic queries
//    List<Book> findByUserId(Long userId);

//    List<Book> findByUserIdAndStatus(Long userId, Book.BookStatus status);

    // Status queries
    List<Book> findByStatus(Book.BookStatus status);

    List<Book> findByListingType(Book.ListingType listingType);

    // Get available books
    @Query("FROM Book WHERE status = 'AVAILABLE'")
    List<Book> findAvailableBooks();

//    Optional<Book> findByIsbn(String isbn);

    // Listing type specific queries
    @Query("FROM Book WHERE listingType = 'SELL_ONLY' OR listingType = 'SELL_AND_LEND'")
    List<Book> findBooksForSale();

    @Query("FROM Book WHERE listingType = 'LEND_ONLY' OR listingType = 'EXCHANGE' OR listingType = 'SELL_AND_LEND'")
    List<Book> findBooksForLending();

//    @Query("FROM Book WHERE listingType = 'EXCHANGE'")
//    List<Book> findBooksForExchange();

    @Query("FROM Book WHERE listingType = 'DONATE'")
    List<Book> findBooksForDonation();

    // Statistics queries
//    Long countByUserId(Long userId);

    // Check if user owns a book
//    boolean existsByBookIdAndUserId(Integer bookId, Long userId);
}