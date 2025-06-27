package Login.repo;


import Login.entity.AllUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface AllUsersRepo extends JpaRepository<AllUsers, Integer> {

    Optional<AllUsers> findOneByEmailAndPassword(String email, String password);

    AllUsers findByEmail(String email);
}
