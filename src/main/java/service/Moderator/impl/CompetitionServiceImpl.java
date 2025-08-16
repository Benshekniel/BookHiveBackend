package service.Moderator.impl;

import model.dto.CompetitionDTO;
import model.entity.Competitions;
import model.repo.AllUsersRepo;
import model.repo.CompetitionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.Moderator.CompetitionService;

import java.util.UUID;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    @Autowired
    private CompetitionRepo competitionRepo;


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
}
