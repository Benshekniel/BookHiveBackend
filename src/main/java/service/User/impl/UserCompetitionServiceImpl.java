package service.User.impl;

import jakarta.transaction.Transactional;
import model.dto.CompetitionDTO;
import model.dto.CompetitionSubmissionsDTO;
import model.entity.CompetitionSubmissions;
import model.entity.Competitions;
import model.repo.AllUsersRepo;
import model.repo.CompetitionRepo;
import model.repo.CompetitionSubmissionsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.User.UserCompetitionService;


import java.util.*;

@Service
public class UserCompetitionServiceImpl implements UserCompetitionService {

    @Autowired
    private CompetitionRepo competitionRepo;

    @Autowired
    private CompetitionSubmissionsRepo competitionSubmissionsRepo;

    public List<Map<String, Object>> getAllUserCompetitionsMapped() {
        return competitionRepo.findAllCompetitionsMapped();
    }

    @Override
    @Transactional
    public String saveSubmitStory(CompetitionSubmissionsDTO competitionSubmissionsDTO) {
        try {
            // Check if this is an update or new submission
            boolean isUpdate = competitionSubmissionsDTO.getSubmissionId() != null;

            if (!isUpdate) {
                // Generate new ID for new submissions
                String uniqueId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                competitionSubmissionsDTO.setSubmissionId(uniqueId);

                // Create new entity
                CompetitionSubmissions competitionSubmissions = new CompetitionSubmissions(
                        competitionSubmissionsDTO.getSubmissionId(),
                        competitionSubmissionsDTO.getCompetitionId(),
                        competitionSubmissionsDTO.getEmail(),
                        competitionSubmissionsDTO.getUserId(),
                        competitionSubmissionsDTO.getTitle(),
                        competitionSubmissionsDTO.getContent()
                );

                // Set default values for new submissions
                competitionSubmissions.setVoteCount(0);
                competitionSubmissions.setVotes(new HashMap<>());
                competitionSubmissions.setFlag(false); // false = draft, true = submitted

                // Save new submission
                competitionSubmissionsRepo.save(competitionSubmissions);

                return "{\"message\": \"success\", \"submissionId\": \"" + uniqueId + "\"}";
            } else {
                // Update existing submission
                int updated = competitionSubmissionsRepo.updateSubmission(
                        competitionSubmissionsDTO.getSubmissionId(),
                        competitionSubmissionsDTO.getTitle(),
                        competitionSubmissionsDTO.getContent()
                );

                if (updated > 0) {
                    return "{\"message\": \"success\", \"submissionId\": \"" + competitionSubmissionsDTO.getSubmissionId() + "\"}";
                } else {
                    return "{\"message\": \"error\", \"error\": \"Submission not found or not updated\"}";
                }
            }
        } catch (Exception e) {
            return "{\"message\": \"error\", \"error\": \"" + e.getMessage() + "\"}";
        }
    }


    // Add this to your UserCompetitionService
    @Override
    public CompetitionSubmissionsDTO getDraftSubmission(String competitionId, String email) {
        Optional<CompetitionSubmissions> submission = competitionSubmissionsRepo.findByCompetitionIdAndEmail(competitionId, email);

        if (submission.isPresent() && !submission.get().getFlag()) { // Check that it's a draft (flag = false)
            CompetitionSubmissions draft = submission.get();

            CompetitionSubmissionsDTO dto = new CompetitionSubmissionsDTO();
            dto.setSubmissionId(draft.getSubmissionId());
            dto.setCompetitionId(draft.getCompetitionId());
            dto.setEmail(draft.getEmail());
            dto.setUserId(draft.getUserId());
            dto.setTitle(draft.getTitle());
            dto.setContent(draft.getContent());

            return dto;
        }

        return null; // No draft found
    }
}
