package service.Competition;

public interface CompetitionsParticipantEmailsService {
    void insertParticipant(String competitionId, String email);
    void updateParticipant(String competitionId, String email);
    void deleteParticipant(String competitionId, String email);
}
