package model.repo.Delivery;

import model.entity.AgentApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentApplicationRepository extends JpaRepository<AgentApplication, Long> {

    List<AgentApplication> findByStatus(AgentApplication.ApplicationStatus status);

    List<AgentApplication> findByStatusOrderByProcessedDateDesc(AgentApplication.ApplicationStatus status);

    List<AgentApplication> findAllByOrderByAppliedDateDesc();

    long countByStatus(AgentApplication.ApplicationStatus status);

    List<AgentApplication> findByEmailIgnoreCase(String email);

    List<AgentApplication> findByPhoneNumber(String phoneNumber);
}