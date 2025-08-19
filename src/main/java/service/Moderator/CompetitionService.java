package service.Moderator;

import model.dto.CompetitionDTO;
import model.entity.Competitions;

import java.util.List;
import java.util.Map;

public interface CompetitionService {

    String createCompetition(CompetitionDTO competitionDTO,String email,String bannerImageName);

    public List<Map<String, Object>> getAllCompetitionsMapped();

}
