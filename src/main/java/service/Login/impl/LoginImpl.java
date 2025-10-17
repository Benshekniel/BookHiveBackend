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
        AllUsers allUsers = allUsersRepo.findByEmail(loginDTO.getEmail());

        if (allUsers != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = allUsers.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);

            if (isPwdRight) {
                if (allUsers.getStatus() == AllUsers.Status.active) {
                    String token = jwtService.generateToken(allUsers.getEmail(), allUsers.getName(), allUsers.getRole(), allUsers.getUser_id());
                    String userRole = allUsers.getRole();
                    return new LoginResponse("Login Success", true, userRole, token);
                } else {
                    String msg;
                    switch (allUsers.getStatus()) {
                        case pending:
                            msg = "Your login is under review. We will get back to you soon.";
                            break;
                        case rejected:
                            msg = "Your application was rejected. Please check your email.";
                            break;
                        case disabled:
                            msg = "Your account has been disabled. Please approach support for more details.";
                            break;
                        case banned:
                            msg = "Your account has been banned!";
                            break;
                        default:
                            msg = "Login Failed";
                            break;
                    }
                    return new LoginResponse(msg, false);
                }
            } else {
                return new LoginResponse("Password does not match", false);
            }
        } else {
            return new LoginResponse("Email does not exist", false);
        }
    }

//    @Override
//    public LoginResponse loginResponse(LoginDto loginDTO) {
//        String msg = "";
//        AllUsers allUsers = allUsersRepo.findByEmail(loginDTO.getEmail());
//        String encodePassword = this.passwordEncoder.encode("agent");
//
//        // Log the encoded password
//        System.out.println("Encoded password: " + encodePassword);
//
//
//        if (allUsers != null) {
//            String password = loginDTO.getPassword();
//            String encodedPassword = allUsers.getPassword();
//            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
//
//
//            if (isPwdRight) {
//                Optional<AllUsers> allUsers1 = allUsersRepo.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
//                if (allUsers1.isPresent()) {
//
//                    String token = jwtService.generateToken(allUsers.getEmail(),allUsers.getName(),allUsers.getRole(),allUsers.getUser_id()); // Assuming this method exists
//                    String userRole = allUsers.getRole();
//                    return new LoginResponse("Login Success", true,userRole,token);
//                } else {
//                    return new LoginResponse("Login Failed", false);
//                }
//            } else {
//                return new LoginResponse("password Not Match", false);
//            }
//        }else {
//            return new LoginResponse("Email not exits", false);
//        }
//    }
}
