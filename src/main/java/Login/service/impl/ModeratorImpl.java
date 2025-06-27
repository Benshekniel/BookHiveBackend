package Login.service.impl;

import Login.dto.LoginDto;
import Login.dto.ModeratorDto;
import Login.entity.Moderator;
import Login.messageResponse.LoginResponse;
import Login.repo.ModeratorRepo;
import Login.service.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModeratorImpl implements ModeratorService {

    @Autowired
    private ModeratorRepo moderatorRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public String addAccount(ModeratorDto moderatorDto) {

        Moderator moderator = new Moderator(
                moderatorDto.getEmployeeid(),
                moderatorDto.getEmployeename(),
                moderatorDto.getEmail(),
                this.passwordEncoder.encode(moderatorDto.getPassword()),
                moderatorDto.getAddress()
        );

        moderatorRepo.save(moderator);


        return moderator.getEmployeename();

    }

    @Override
    public LoginResponse loginEmployee(LoginDto loginDTO) {
        String msg = "";
        Moderator moderator1 = moderatorRepo.findByEmail(loginDTO.getEmail());
        if (moderator1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = moderator1.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                Optional<Moderator> employee = moderatorRepo.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (employee.isPresent()) {
                    return new LoginResponse("Login Success", true);
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
