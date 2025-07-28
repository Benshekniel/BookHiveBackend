package service.SignUp.impl;

import model.dto.ModeratorDto;
import model.entity.AllUsers;
import model.entity.Moderator;
import model.repo.AllUsersRepo;
import model.repo.ModeratorRepo;
import model.repo.OrgRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import service.SignUp.Register_ModeratorAccount;

@Service
public class Moderator_RegisterImpl implements Register_ModeratorAccount {

    @Autowired
    private AllUsersRepo allUsersRepo;

    @Autowired
    private ModeratorRepo moderatorRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String createModerator(ModeratorDto moderatorDto) {

        AllUsers allUsers = allUsersRepo.findByEmail(moderatorDto.getEmail());
        if (allUsers != null) {
            return "email already in use";
        }

        Moderator moderator = new Moderator(
                moderatorDto.getName(),
                moderatorDto.getEmail(),
                this.passwordEncoder.encode(moderatorDto.getPassword()),
                moderatorDto.getPhone(),
                moderatorDto.getDob(),
                moderatorDto.getCity(),
                moderatorDto.getExperience(),
                moderatorDto.getAddress()
        );

        moderatorRepo.save(moderator);
        return "success";
    }
}
