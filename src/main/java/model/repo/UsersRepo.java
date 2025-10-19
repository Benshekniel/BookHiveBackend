package model.repo;

import model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {

    Optional<Users> findOneByEmailAndPassword(String email, String password);

    Optional<Users> findByEmail(String email);
}
