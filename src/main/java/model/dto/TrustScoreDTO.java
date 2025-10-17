package model.dto;

public class TrustScoreDTO {

    private Long userId;
    private Integer score;
    private String email;

    public TrustScoreDTO(Long userId, Integer score, String email) {
        this.userId = userId;
        this.score = score;
        this.email = email;
    }

    public TrustScoreDTO() {
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
        return "TrustScoreDTO{" +
                "userId=" + userId +
                ", score=" + score +
                ", email='" + email + '\'' +
                '}';
    }
}
