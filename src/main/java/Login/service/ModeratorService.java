package Login.service;

import Login.dto.LoginDto;
import Login.dto.ModeratorDto;
import Login.messageResponse.LoginResponse;

public interface ModeratorService {
    String addAccount(ModeratorDto moderatorDto);

    LoginResponse loginEmployee(LoginDto loginDTO);

}
