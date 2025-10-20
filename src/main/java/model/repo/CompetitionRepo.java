package model.repo;


import jakarta.transaction.Transactional;
import model.entity.Competitions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
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
        c.pause_status AS pauseStatus,
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

    // ✅ Activate competition
    @Modifying
    @Query("UPDATE Competitions c " +
            "SET c.activeStatus = true, c.activatedAt = CURRENT_TIMESTAMP " +
            "WHERE c.competitionId = :competitionId AND c.createdBy = :email")
    int activateCompetition(@Param("competitionId") String competitionId, @Param("email") String email);

    // ✅ Pause competition
    @Modifying
    @Query("UPDATE Competitions c SET c.pauseStatus = true WHERE c.competitionId = :competitionId AND c.createdBy = :email")
    int pauseCompetition(@Param("competitionId") String competitionId, @Param("email") String email);

    //  Reactivate competition (activeStatus = false, don't touch activatedAt)
    @Modifying
    @Query("UPDATE Competitions c SET c.activeStatus = true WHERE c.competitionId = :competitionId AND c.createdBy = :email")
    int re_activateCompetition(@Param("competitionId") String competitionId, @Param("email") String email);

    // ❌ Deactivate competition (activeStatus = false, don't touch activatedAt)
    @Modifying
    @Query("UPDATE Competitions c SET c.activeStatus = false WHERE c.competitionId = :competitionId AND c.createdBy = :email")
    int deactivateCompetition(@Param("competitionId") String competitionId, @Param("email") String email);

    // ❌ Unpause competition (pauseStatus = false)
    @Modifying
    @Query("UPDATE Competitions c SET c.pauseStatus = false WHERE c.competitionId = :competitionId AND c.createdBy = :email")
    int unpauseCompetition(@Param("competitionId") String competitionId, @Param("email") String email);


    // ✅ Fetch only active competitions
    @Query("SELECT c FROM Competitions c WHERE c.activeStatus = true")
    List<Competitions> findAllActiveCompetitions();

    // ✅ Fetch active competitions (active_status = true and not paused) for Organization Dashboard
    @Query("SELECT c FROM Competitions c WHERE c.activeStatus = true AND c.pauseStatus = false ORDER BY c.createdAt DESC")
    List<Competitions> findActiveCompetitions();

    // ✅ Fetch competitions created by a specific moderator
    @Query("SELECT c FROM Competitions c WHERE c.createdBy = :createdBy")
    List<Competitions> findCompetitionsByCreator(String createdBy);

    // ✅ Fetch competition by ID (optional shortcut)
    @Query("SELECT c FROM Competitions c WHERE c.competitionId = :competitionId")
    Competitions findCompetitionById(String competitionId);

    // ✅ Increment currentParticipants by 1
    @Modifying
    @Transactional
    @Query("UPDATE Competitions c SET c.currentParticipants = c.currentParticipants + 1 WHERE c.competitionId = :competitionId")
    int incrementParticipants(@Param("competitionId") String competitionId);

    // ✅ Decrement currentParticipants by 1 (only if > 0)
    @Modifying
    @Transactional
    @Query("UPDATE Competitions c SET c.currentParticipants = c.currentParticipants - 1 WHERE c.competitionId = :competitionId AND c.currentParticipants > 0")
    int decrementParticipants(@Param("competitionId") String competitionId);

    // ✅ Get all competitions a user participates in
    @Query(value = """
        SELECT c.* 
        FROM competitions c
        INNER JOIN competitions_participant_emails p 
        ON c.competition_id = p.competition_id
        WHERE p.email = :email
    """, nativeQuery = true)
    List<Map<String, Object>> findCompetitionsByParticipant(@Param("email") String email);
}
