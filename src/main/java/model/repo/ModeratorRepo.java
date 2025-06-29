package model.repo;

import model.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface ModeratorRepo extends JpaRepository<Moderator, Integer> {

    Optional<Moderator> findOneByEmailAndPassword(String email, String password);

    Moderator findByEmail(String email);

}
