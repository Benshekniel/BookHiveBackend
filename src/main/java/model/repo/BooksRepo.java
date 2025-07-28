package model.repo;

import model.entity.AllUsers;
import model.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@EnableJpaRepositories
@Repository
public interface BooksRepo extends JpaRepository<Books, Long> {

    Optional<Books> findOneByRoleAndEmail(String role, String email);

    Books findByIsbn(String isbn);


}
