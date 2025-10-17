package model.repo;


import jakarta.transaction.Transactional;
import model.entity.competitionsParticipantEmails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface CompetitionsParticipantEmailsRepo extends JpaRepository<competitionsParticipantEmails, String> {

    // Insert new participant (competitionId + email)
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO competitions_participant_emails (competition_id, email) VALUES (?1, ?2)", nativeQuery = true)
    void insertParticipant(String competitionId, String email);

    // Update email for a given competitionId
    @Modifying
    @Transactional
    @Query(value = "UPDATE competitions_participant_emails SET email = ?2 WHERE competition_id = ?1", nativeQuery = true)
    void updateParticipantEmail(String competitionId, String email);

    // Delete specific participant record using both competitionId and email
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM competitions_participant_emails WHERE competition_id = ?1 AND email = ?2", nativeQuery = true)
    void deleteParticipant(String competitionId, String email);

}
