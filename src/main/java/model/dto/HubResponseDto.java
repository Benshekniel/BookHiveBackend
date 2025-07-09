package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubResponseDto {
    private Long hubId;
    private String name;
    private String address;
    private String city;
    private Integer numberOfRoutes;
    private LocalDateTime createdAt;
    private Long hubManagerId;
    private String hubManagerName;
    private Long totalAgents;
    private Long activeAgents;
    private Long totalDeliveries;
}
