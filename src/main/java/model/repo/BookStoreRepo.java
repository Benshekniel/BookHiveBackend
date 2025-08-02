package model.repo;

import model.entity.AllUsers;
import model.entity.BookStore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BookStoreRepo extends JpaRepository<BookStore, Integer> {

    Optional <BookStore> findByStoreId(Integer storeId);

}
