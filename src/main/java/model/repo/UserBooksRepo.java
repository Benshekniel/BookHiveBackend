package model.repo;

import model.entity.Books;
import model.entity.UserBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@EnableJpaRepositories
@Repository
public interface UserBooksRepo extends JpaRepository<UserBooks, Long> {

    Optional<UserBooks> findOneByUserEmailAndBookId(String userEmail, Long bookId);

    UserBooks findByIsbn(String isbn);


}