package Login.service;

import Login.dto.LoginDto;
import Login.messageResponse.LoginResponse;

public interface LoginService {

    LoginResponse loginResponse(LoginDto loginDTO);

}
