package service.SignUp.impl;


import model.dto.OrgDTO;
import model.dto.UsersDto;
import model.entity.AllUsers;
import model.entity.Organization;
import model.entity.Users;
import model.repo.AllUsersRepo;
import model.repo.OrgRepo;
import model.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import service.SignUp.Register_OrgAccount;
import service.SignUp.Register_UserAccount;

import java.util.Optional;

@Service
public class User_RegisterImpl implements Register_UserAccount {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private AllUsersRepo allUsersRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String createUser(UsersDto usersDto) {
        // Check if email already exists in all users
        AllUsers allUsers = allUsersRepo.findByEmail(usersDto.getEmail());
        if (allUsers != null) {
            return "email already in use";
        }

        // If both checks pass, save new organization
        Users users = new Users(

                usersDto.getEmail(),
                this.passwordEncoder.encode(usersDto.getPassword()),
                usersDto.getFname() + " " + usersDto.getLname(),
                usersDto.getFname(),
                usersDto.getLname(),
                usersDto.getPhone(),
                usersDto.getDob(),
                usersDto.getIdType(),
                usersDto.getIdFront(),
                usersDto.getIdBack(),
                usersDto.getGender(),
                usersDto.getAddress(),
                usersDto.getCity(),
                usersDto.getState(),
                usersDto.getZip(),
                usersDto.getBillImage()
        );

        usersRepo.save(users);
        return "success";
    }
}
