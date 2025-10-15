package service.TrustScore.impl;

import model.entity.AllUsers;
import model.entity.TrustScore;
import model.repo.AllUsersRepo;
import model.repo.TrustScoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.TrustScore.TrustScoreService;

@Service
public class TrustScoreImpl implements TrustScoreService {

    @Autowired
    private TrustScoreRepo trustScoreRepo;

    @Autowired
    private AllUsersRepo allUsersRepo;

    @Override
    public void initial_Trustscore(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        // Check if trust score already exists for this email
        TrustScore existingTrustScore = trustScoreRepo.findByEmail(email);
        if (existingTrustScore != null) {
            throw new IllegalStateException("Trust score already initialized for email: " + email);
        }

        // ✅ Fetch user from AllUsersRepo
        AllUsers user = allUsersRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalStateException("User not found with email: " + email);
        }

        // ✅ Get userId from user entity
        // Convert int -> Long
        Long userId = Long.valueOf(user.getUser_id());

        // ✅ Create and save new TrustScore
        TrustScore trustScore = new TrustScore(email, 500, userId);
        trustScoreRepo.save(trustScore);
    }

    @Override
    public void adjust_Trustscore(String email, Integer finalAmount) {
        if (email == null || finalAmount == null) {
            throw new IllegalArgumentException("Email and finalAmount cannot be null");
        }
        if (finalAmount < 0) {
            throw new IllegalArgumentException("Final trust score cannot be negative");
        }

        TrustScore trustScore = trustScoreRepo.findByEmail(email);
        if (trustScore == null) {
            throw new IllegalStateException("No trust score found for email: " + email);
        }

        trustScore.setScore(finalAmount);
        trustScoreRepo.save(trustScore);
    }

    @Override
    public Integer fetch_Trustscore(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        TrustScore trustScore = trustScoreRepo.findByEmail(email);
        if (trustScore == null) {
            return null; // Or throw an exception if preferred
        }
        return trustScore.getScore();
    }

    @Override
    public void deduct_Trustscore(String email, Integer negativeAmount) {
        if (email == null || negativeAmount == null) {
            throw new IllegalArgumentException("Email and negativeAmount cannot be null");
        }
        if (negativeAmount < 0) {
            throw new IllegalArgumentException("Negative amount must be a positive value");
        }

        TrustScore trustScore = trustScoreRepo.findByEmail(email);
        if (trustScore == null) {
            throw new IllegalStateException("No trust score found for email: " + email);
        }

        int newScore = trustScore.getScore() - negativeAmount;
        if (newScore < 0) {
            throw new IllegalStateException("Trust score cannot be reduced below 0");
        }

        trustScore.setScore(newScore);
        trustScoreRepo.save(trustScore);
    }

    @Override
    public void add_Trustscore(String email, Integer positiveAmount) {
        if (email == null || positiveAmount == null) {
            throw new IllegalArgumentException("Email and positiveAmount cannot be null");
        }
        if (positiveAmount < 0) {
            throw new IllegalArgumentException("Positive amount must be a positive value");
        }

        TrustScore trustScore = trustScoreRepo.findByEmail(email);
        if (trustScore == null) {
            throw new IllegalStateException("No trust score found for email: " + email);
        }

        int newScore = trustScore.getScore() + positiveAmount;
        trustScore.setScore(newScore);
        trustScoreRepo.save(trustScore);
    }
}
