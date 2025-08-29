package model.repo;

import model.entity.AllUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface AllUsersRepo extends JpaRepository<AllUsers, Integer> {

    Optional<AllUsers> findOneByEmailAndPassword(String email, String password);

    AllUsers findByEmail(String email);

//    @Query("SELECT u.name AS name, u.email AS email, u.role AS role FROM AllUsers u WHERE u.status = 'pending'")
//    List<Map<String, Object>> findAllPending();

    // Additional methods for application approval process
    List<AllUsers> findByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);
}