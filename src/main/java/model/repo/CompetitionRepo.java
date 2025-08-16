package model.repo;

import model.entity.Books;
import model.entity.Competitions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@EnableJpaRepositories
@Repository
public interface CompetitionRepo extends JpaRepository<Competitions, String> {
}
