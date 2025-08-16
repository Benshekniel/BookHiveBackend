package service.Moderator;

import model.dto.CompetitionDTO;

public interface CompetitionService {

    String createCompetition(CompetitionDTO competitionDTO,String email,String bannerImageName);
}
