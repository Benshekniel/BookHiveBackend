package service.User;

import model.dto.CompetitionDTO;
import model.entity.Competitions;

import java.util.List;
import java.util.Map;

public interface UserCompetitionService {

    /**
     * Fetches all active and unpaused competitions for users.
     * @return List of Map objects containing competition details.
     */
    public List<Map<String, Object>> getAllUserCompetitionsMapped();

}
