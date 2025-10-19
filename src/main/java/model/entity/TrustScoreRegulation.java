package model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name="trust_score_regulation")
@NoArgsConstructor
public class TrustScoreRegulation {

    @Id
    @Column(name = "id") // Derived from user_id ( users table )
    private Long id;

    @Column(nullable = true,name = "rule")
    private String rule;

    @Column(nullable = true,name = "amount")
    private Integer amount;

}
