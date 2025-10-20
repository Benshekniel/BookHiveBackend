package service.Moderator;

import model.entity.TrustScore;

public interface TrustScoreRegulationService {

    public Integer getAmountForRule(String rule);

    void increaseReview(String email);
    void increasePurchase(String email);
    void increaseCompJoin(String email);
    void decreaseNegative(String email);
    void increasePositive(String email);

    TrustScore getTrustScoreByEmail(String email);

    void updateReviewAmount(Integer amount);
    void updatePurchaseAmount(Integer amount);
    void updateCompJoinAmount(Integer amount);
    void updateNegativeAmount(Integer amount);
    void updatePositiveAmount(Integer amount);
}
