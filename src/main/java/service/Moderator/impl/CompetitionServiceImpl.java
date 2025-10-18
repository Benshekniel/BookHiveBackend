package service.Moderator.impl;

import jakarta.transaction.Transactional;
import model.dto.CompetitionDTO;
import model.entity.CompetitionSubmissions;
import model.entity.Competitions;
import model.repo.AllUsersRepo;
import model.repo.CompetitionRepo;
import model.repo.CompetitionSubmissionsRepo;
import model.repo.CompetitionsParticipantEmailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.Moderator.CompetitionService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    @Autowired
    private CompetitionRepo competitionRepo;

    @Autowired
    private CompetitionSubmissionsRepo competitionSubmissionsRepo;

    @Autowired
    private CompetitionsParticipantEmailsRepo participantEmailsRepo;


    @Override
    public String createCompetition(CompetitionDTO competitionDTO,String email,String bannerImageName) {

        String uniqueId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        competitionDTO.setCompetitionId(uniqueId);
        competitionDTO.setBannerImage(bannerImageName);
        competitionDTO.setCreatedBy(email);

        Competitions competition = new Competitions(
                competitionDTO.getCompetitionId(),
                false,
                competitionDTO.getCreatedBy(),
                competitionDTO.getTitle(),
                competitionDTO.getEntryTrustScore(),
                competitionDTO.getPrizeTrustScore(),
                competitionDTO.getStartDateTime(),
                competitionDTO.getEndDateTime(),
                competitionDTO.getVotingStartDateTime(),
                competitionDTO.getVotingEndDateTime(),
                competitionDTO.getMaxParticipants(),
                0,
                null,
                competitionDTO.getTheme(),
                competitionDTO.getRules(),
                competitionDTO.getJudgingCriteria(),
                competitionDTO.getBannerImage(),
                competitionDTO.getDescription()

        );
        competitionRepo.save(competition);
        return "success";
    }

    public List<Map<String, Object>> getAllCompetitionsMapped() {
        return competitionRepo.findAllCompetitionsMapped();
    }

    @Transactional
    public String makeActive(String competitionId, String email) {
        int updated = competitionRepo.activateCompetition(competitionId, email);
        return updated > 0 ? "success" : "competition not found or email mismatch";
    }

    @Transactional
    public String make_ReActive(String competitionId, String email) {
        int updated = competitionRepo.re_activateCompetition(competitionId, email);
        return updated > 0 ? "success" : "competition not found or email mismatch";
    }

    @Transactional
    public String stopActive(String competitionId, String email) {
        int updated = competitionRepo.deactivateCompetition(competitionId, email);
        return updated > 0 ? "success" : "competition not found or email mismatch";
    }

    @Transactional
    public String makePause(String competitionId, String email) {
        int updated = competitionRepo.pauseCompetition(competitionId, email);
        return updated > 0 ? "success" : "competition not found or email mismatch";
    }

    @Transactional
    public String makeResume(String competitionId, String email) {
        int updated = competitionRepo.unpauseCompetition(competitionId, email);
        return updated > 0 ? "success" : "competition not found or email mismatch";
    }

    @Transactional
    public String joinCompetition(String competitionId, String email) {
        try {
            // Check if competition exists
            boolean exists = competitionRepo.existsById(competitionId);
            if (!exists) {
                return "competition_not_found";
            }

            // Add participant to the competition
            participantEmailsRepo.insertParticipant(competitionId, email);

            // Step 2: increment competition participant count
            competitionRepo.incrementParticipants(competitionId);

            return "joined_successfully";
        } catch (Exception e) {
            // Handle duplicate entry or SQL exceptions
            return "error: " + e.getMessage();
        }
    }

    @Transactional
    public String leaveCompetition(String competitionId, String email) {
        try {
            participantEmailsRepo.deleteParticipant(competitionId, email);
            // Step 2: decrement competition participant count
            competitionRepo.decrementParticipants(competitionId);
            return "left_successfully";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @Override
    @Transactional
    public List<String> getCompetitionsByEmail(String email) {
        return participantEmailsRepo.findCompetitionIdsByEmail(email);
    }

    // Get all submissions by email
    @Override
    @Transactional
    public List<CompetitionSubmissions> getSubmissionsByEmail(String email) {
        return competitionSubmissionsRepo.findAllByEmail(email);
    }

    // ✅ Fetch all competitions that the user participates in
    @Transactional
    public List<Map<String, Object>> getUserParticipatingCompetitions(String email) {
        return competitionRepo.findCompetitionsByParticipant(email);
    }

}
