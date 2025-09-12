package model.repo;

import model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface OrgRepo extends JpaRepository<Organization, Long> {

    Optional<Organization> findOneByEmailAndPassword(String email, String password);

    Optional<Organization> findByRegNo(String regNo);

    Optional<Organization> findByEmail(String email);
}

