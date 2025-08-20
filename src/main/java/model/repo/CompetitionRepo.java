package model.repo;

import model.entity.Books;
import model.entity.Competitions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@EnableJpaRepositories
@Repository
public interface CompetitionRepo extends JpaRepository<Competitions, String> {


    // ✅ Fetch all competitions with ALL fields in name:value format
    @Query(value = """
    SELECT 
        c.competition_id AS competitionId,
        c.active_status AS activeStatus,
        c.created_by AS createdBy,
        c.created_at AS createdAt,
        c.activated_at AS activatedAt,
        c.title AS title,
        c.entry_trust_score AS entryTrustScore,
        c.prize_trust_score AS prizeTrustScore,
        c.start_date_time AS startDateTime,
        c.end_date_time AS endDateTime,
        c.voting_start_date_time AS votingStartDateTime,
        c.voting_end_date_time AS votingEndDateTime,
        c.max_participants AS maxParticipants,
        c.current_participants AS currentParticipants,
        COALESCE(c.participant_emails, '[]'::jsonb) AS participantEmails,
        c.theme AS theme,
        c.rules AS rules,
        c.judging_criteria AS judgingCriteria,
        c.banner_image AS bannerImage,
        c.description AS description
    FROM competitions c
    """,
            nativeQuery = true)
    List<Map<String, Object>> findAllCompetitionsMapped();


    // ✅ Fetch only active competitions
    @Query("SELECT c FROM Competitions c WHERE c.activeStatus = true")
    List<Competitions> findAllActiveCompetitions();

    // ✅ Fetch competitions created by a specific moderator
    @Query("SELECT c FROM Competitions c WHERE c.createdBy = :createdBy")
    List<Competitions> findCompetitionsByCreator(String createdBy);

    // ✅ Fetch competition by ID (optional shortcut)
    @Query("SELECT c FROM Competitions c WHERE c.competitionId = :competitionId")
    Competitions findCompetitionById(String competitionId);
}
