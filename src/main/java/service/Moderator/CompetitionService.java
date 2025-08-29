package service.Moderator;

import model.dto.CompetitionDTO;
import model.entity.Competitions;

import java.util.List;
import java.util.Map;

public interface CompetitionService {

    String createCompetition(CompetitionDTO competitionDTO,String email,String bannerImageName);

    public List<Map<String, Object>> getAllCompetitionsMapped();

    public String makeActive(String competitionId,String email);

    public String make_ReActive(String competitionId,String email);

    public String stopActive(String competitionId,String email);

    public String makePause(String competitionId,String email);

    public String makeResume(String competitionId,String email);


}
