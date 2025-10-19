package model.repo.BookStore;

import model.entity.AllUsers;
import model.entity.BookStore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BookStoreRepo extends JpaRepository<BookStore, Integer> {

    Optional <BookStore> findByStoreId(Integer storeId);

    @Query(value = "SELECT * FROM bookstores WHERE user_id = :userId", nativeQuery = true)
    BookStore findByAllUserNew(@Param("userId") Integer userId);

}
