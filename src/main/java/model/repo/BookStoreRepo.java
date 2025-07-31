package model.repo;

import model.entity.BookStore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStoreRepo extends JpaRepository<BookStore, Integer> {

//    BookStore findByStoreId(Integer storeId);

}
