package model.repo;

import model.entity.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {

    Optional <Book> findByBookId(Integer bookId);

}
