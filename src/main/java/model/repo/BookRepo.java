package model.repo;

import model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Integer> {
    Book findByTitle(String title);
}
