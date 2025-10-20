package service.Competition.impl;

import model.repo.CompetitionsParticipantEmailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.Competition.CompetitionsParticipantEmailsService;

@Service
public class CompetitionsParticipantEmailsImpl implements CompetitionsParticipantEmailsService {

    @Autowired
    private CompetitionsParticipantEmailsRepo repo;

    @Override
    public void insertParticipant(String competitionId, String email) {
        repo.insertParticipant(competitionId, email);
        System.out.println("✅ Inserted participant: " + email + " for competition ID: " + competitionId);
    }

    @Override
    public void updateParticipant(String competitionId, String email) {
        repo.updateParticipantEmail(competitionId, email);
        System.out.println("✏️ Updated participant email for competition ID: " + competitionId + " to " + email);
    }

    @Override
    public void deleteParticipant(String competitionId, String email) {
        repo.deleteParticipant(competitionId, email);
        System.out.println("🗑️ Deleted participant: " + email + " from competition ID: " + competitionId);
    }
}
