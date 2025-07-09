package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentUpdateDto {
    private String name;
    private String phone;
    private String address;
    private String nicPhoto;
    private String status;
    private String vehicleType;
    private String vehicleNumber;
    private String availabilityStatus;
}
