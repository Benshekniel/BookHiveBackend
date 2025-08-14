package model.repo;

import model.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface ModeratorRepo extends JpaRepository<Moderator, Long> {

    Optional<Moderator> findOneByEmailAndPassword(String email, String password);

    Moderator findByEmail(String email);

    @Query("SELECT a.name AS name, a.email AS email, a.role AS role, " +
            "u.fname AS fname, u.lname AS lname, u.phone AS phone, u.dob AS dob, u.idType AS idType, " +
            "u.idFront AS idFront, u.idBack AS idBack, u.gender AS gender, u.address AS address, " +
            "u.city AS city, u.state AS state, u.zip AS zip, u.billImage AS billImage, u.createdAt AS createdAt " +
            "FROM AllUsers a LEFT JOIN Users u ON a.email = u.email WHERE a.status = 'pending' AND a.role = 'user'")
    List<Map<String, Object>> findAllPending();

}
