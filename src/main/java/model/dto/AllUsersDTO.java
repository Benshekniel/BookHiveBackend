package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllUsersDTO {

    private int user_id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String phoneNumber; // Optional
    private String address;

}
