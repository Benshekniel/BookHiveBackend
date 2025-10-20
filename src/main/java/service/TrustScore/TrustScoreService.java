package service.TrustScore;

public interface TrustScoreService {

    void initial_Trustscore(String email);
    void adjust_Trustscore(String email, Integer finalAmount);
    Integer fetch_Trustscore(String email);
    void deduct_Trustscore(String email, Integer negativeAmount);
    void add_Trustscore(String email, Integer positiveAmount);
}

