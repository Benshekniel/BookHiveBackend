package model.repo;

import model.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HubRepository extends JpaRepository<Hub, Long> {
    List<Hub> findByCity(String city);

    @Query("SELECT h FROM Hub h WHERE h.name LIKE %:name%")
    List<Hub> findByNameContaining(@Param("name") String name);
}
