package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import model.entity.agent.Agent;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentCreateDto {
    private Long hubId;
    private Long userId;
    private Agent.VehicleType vehicleType;
    private String vehicleNumber;
}
