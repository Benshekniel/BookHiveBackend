package model.entity;


import jakarta.persistence.*;

@Entity
@Table(name="trust_score")
public class TrustScore {

    @Id
    @Column(name = "user_id") // Derived from user_id ( users table )
    private Long userId;

    @Column(name="score", length = 255)
    private Integer score;

    @Column(name = "email", nullable = true, unique = true)
    private String email;

    public TrustScore(String email, Integer score, Long userId) {
        this.email = email;
        this.score = score;
        this.userId = userId;
    }

    public TrustScore() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "TrustScore{" +
                "userId=" + userId +
                ", score=" + score +
                ", email='" + email + '\'' +
                '}';
    }
}
