package model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubUpdateDto {
    private String name;
    private String address;
    private String city;
    private Integer numberOfRoutes;
}
