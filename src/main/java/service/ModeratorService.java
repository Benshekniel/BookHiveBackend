package service;

import model.dto.LoginDto;
import model.dto.ModeratorDto;
import model.messageResponse.LoginResponse;

public interface ModeratorService {
    String addAccount(ModeratorDto moderatorDto);

    LoginResponse loginEmployee(LoginDto loginDTO);

}
