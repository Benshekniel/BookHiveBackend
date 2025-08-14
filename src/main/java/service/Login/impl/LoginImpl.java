package service.Login.impl;

import model.dto.LoginDto;
import model.entity.AllUsers;
import model.messageResponse.LoginResponse;
import model.repo.AllUsersRepo;
import service.Jwt.JwtService;
import service.Login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginImpl implements LoginService {

    @Autowired
    private AllUsersRepo allUsersRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Override
    public LoginResponse loginResponse(LoginDto loginDTO) {
        String msg = "";
        AllUsers allUsers = allUsersRepo.findByEmail(loginDTO.getEmail());

        if (allUsers != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = allUsers.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);


            if (isPwdRight) {
                Optional<AllUsers> allUsers1 = allUsersRepo.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (allUsers1.isPresent()) {

                    String token = jwtService.generateToken(allUsers.getEmail(),allUsers.getName(),allUsers.getRole(),allUsers.getUser_id()); // Assuming this method exists
                    String userRole = allUsers.getRole();
                    return new LoginResponse("Login Success", true,userRole,token);
                } else {
                    return new LoginResponse("Login Failed", false);
                }
            } else {
                return new LoginResponse("password Not Match", false);
            }
        }else {
            return new LoginResponse("Email not exits", false);
        }
    }
}
