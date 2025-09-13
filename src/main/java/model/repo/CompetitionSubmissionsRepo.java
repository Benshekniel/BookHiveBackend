package model.repo;

import jakarta.transaction.Transactional;
import model.entity.CompetitionSubmissions;
import model.entity.Competitions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface CompetitionSubmissionsRepo extends JpaRepository<CompetitionSubmissions, String> {

    // Update an existing submission if it exists (by submissionId)
    @Modifying
    @Transactional
    @Query("UPDATE CompetitionSubmissions cs SET cs.title = :title, cs.content = :content WHERE cs.submissionId = :submissionId")
    int updateSubmission(
            @Param("submissionId") String submissionId,
            @Param("title") String title,
            @Param("content") String content
    );

    // Find submissions by competitionId
    List<CompetitionSubmissions> findByCompetitionId(String competitionId);

    // Find submissions by userId
    List<CompetitionSubmissions> findByUserId(String userId);

    // Find submissions by competitionId and userId (to check if user has already submitted)
    Optional<CompetitionSubmissions> findByCompetitionIdAndUserId(String competitionId, String userId);

    // Find submissions by competitionId and email (to check if user has already submitted)
    Optional<CompetitionSubmissions> findByCompetitionIdAndEmail(String competitionId, String email);


    // Find drafts by userId
    @Query("SELECT cs FROM CompetitionSubmissions cs WHERE cs.userId = :userId AND cs.flag = false")
    List<CompetitionSubmissions> findDraftsByUserId(@Param("userId") String userId);

    // Find submitted entries by userId
    @Query("SELECT cs FROM CompetitionSubmissions cs WHERE cs.userId = :userId AND cs.flag = true")
    List<CompetitionSubmissions> findSubmittedByUserId(@Param("userId") String userId);

    // Delete a submission
    @Modifying
    @Transactional
    @Query("DELETE FROM CompetitionSubmissions cs WHERE cs.submissionId = :submissionId AND cs.userId = :userId")
    int deleteSubmission(@Param("submissionId") String submissionId, @Param("userId") String userId);
}
