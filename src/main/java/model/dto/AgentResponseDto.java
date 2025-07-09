package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import model.entity.agent.Agent;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponseDto {
    private Long agentId;
    private Long hubId;
    private String hubName;
    private Long userId;
    private String userName;
    private String userEmail;
    private Agent.VehicleType vehicleType;
    private String vehicleNumber;
    private Agent.AvailabilityStatus availabilityStatus;
    private Double trustScore;
    private Integer deliveryTime;
    private Integer numberOfDelivery;
    private double rating;
    private String userPhone;
}
