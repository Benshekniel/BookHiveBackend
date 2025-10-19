package model.repo.BookStore;

import model.entity.BookStore;

import jakarta.transaction.Transactional;
import model.entity.AllUsers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BookStoreRepo extends JpaRepository<BookStore, Integer> {

    Optional <BookStore> findByStoreId(Integer storeId);

    @Query(value = "SELECT * FROM bookstores WHERE user_id = :userId", nativeQuery = true)
    BookStore findByAllUserNew(@Param("userId") Integer userId);


    // ✅ Update user_id in bookstore where email matches
    @Modifying
    @Transactional
    @Query(value = "UPDATE bookstores SET user_id = :userId WHERE email = :email", nativeQuery = true)
    int updateUserIdByEmail(@Param("userId") Integer userId, @Param("email") String email);

    // ✅ Get user_id from all_users by email
    @Query("SELECT a.user_id FROM AllUsers a WHERE a.email = :email")
    Integer findUserIdByEmailinAllUsers(@Param("email") String email);

}
