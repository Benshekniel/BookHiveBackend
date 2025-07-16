package service.Login;

import model.dto.LoginDto;
import model.messageResponse.LoginResponse;

public interface LoginService {

    LoginResponse loginResponse(LoginDto loginDTO);

}
