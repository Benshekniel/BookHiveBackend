package model.repo;

import model.entity.BSBook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * For BookStore users' books.
 */
@Repository
public interface BSBookRepo extends JpaRepository<BSBook, Integer> {

    Optional <BSBook> findByBookId(Integer bookId);

}
