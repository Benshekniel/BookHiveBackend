package service.Moderator.impl;

import jakarta.transaction.Transactional;
import model.entity.TrustScoreRegulation;
import model.repo.TrustScoreRegulationRepository;
import model.repo.TrustScoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import model.entity.TrustScore;
import service.Moderator.TrustScoreRegulationService;

@Service
public class TrustScoreRegulationImpl implements TrustScoreRegulationService {


    @Autowired
    private TrustScoreRepo trustScoreRepo;

    @Autowired
    private TrustScoreRegulationRepository trustScoreRegulationRepository;


    @Override
    public Integer getAmountForRule(String rule) {
        return trustScoreRegulationRepository.findAmountByRule(rule);
    }

    @Override
    @Transactional
    public void increaseReview(String email) {
        applyScoreChange(email, "REVIEW", true);
    }

    @Override
    @Transactional
    public void increasePurchase(String email) {
        applyScoreChange(email, "PURCHASE", true);
    }

    @Override
    @Transactional
    public void increaseCompJoin(String email) {
        applyScoreChange(email, "COMPJOIN", true);
    }

    @Override
    @Transactional
    public void decreaseNegative(String email) {
        applyScoreChange(email, "NEGATIVE", false);
    }

    @Override
    @Transactional
    public void increasePositive(String email) {
        applyScoreChange(email, "POSITIVE", true);
    }

    private void applyScoreChange(String email, String rule, boolean increase) {
        TrustScore user = trustScoreRepo.findByEmail(email);
        if (user == null) return;

        Integer currentScore = user.getScore() != null ? user.getScore() : 0;
        Integer amount = getAmountForRule(rule);
        if (amount == null) amount = 0;

        int updatedScore = increase ? currentScore + amount : currentScore - amount;
        user.setScore(updatedScore);
        trustScoreRepo.save(user);
    }

    @Override
    public TrustScore getTrustScoreByEmail(String email) {
        return trustScoreRepo.findByEmail(email);
    }

    @Override
    public void updateReviewAmount(Integer amount) {
        trustScoreRegulationRepository.updateReviewAmount(amount);
    }

    @Override
    public void updatePurchaseAmount(Integer amount) {
        trustScoreRegulationRepository.updatePurchaseAmount(amount);
    }

    @Override
    public void updateCompJoinAmount(Integer amount) {
        trustScoreRegulationRepository.updateCompJoinAmount(amount);
    }

    @Override
    public void updateNegativeAmount(Integer amount) {
        trustScoreRegulationRepository.updateNegativeAmount(amount);
    }

    @Override
    public void updatePositiveAmount(Integer amount) {
        trustScoreRegulationRepository.updatePositiveAmount(amount);
    }


}
