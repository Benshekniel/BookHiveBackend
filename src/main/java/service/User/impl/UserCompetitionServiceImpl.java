package service.User.impl;

import jakarta.transaction.Transactional;
import model.dto.CompetitionDTO;
import model.entity.Competitions;
import model.repo.AllUsersRepo;
import model.repo.CompetitionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.User.UserCompetitionService;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserCompetitionServiceImpl implements UserCompetitionService {

    @Autowired
    private CompetitionRepo competitionRepo;

    public List<Map<String, Object>> getAllUserCompetitionsMapped() {
        return competitionRepo.findAllCompetitionsMapped();
    }
}
