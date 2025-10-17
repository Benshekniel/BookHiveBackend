package model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "competitions_participant_emails")
public class competitionsParticipantEmails {

    @Id
    @Column(name = "competition_id", nullable = false)
    private String competitionId;

    @Column(name = "email", nullable = true)
    private String email;

    public competitionsParticipantEmails() {
    }

    public competitionsParticipantEmails(String competitionId, String email) {
        this.competitionId = competitionId;
        this.email = email;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "competitionsParticipantEmails{" +
                "competitionId='" + competitionId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
