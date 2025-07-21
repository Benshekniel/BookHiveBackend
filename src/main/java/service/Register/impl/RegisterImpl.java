package service.Register.impl;

import model.dto.AllUsersDTO;
import model.entity.AllUsers;
import model.repo.AllUsersRepo;
import service.Register.RegisterAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterImpl implements RegisterAccount {

    @Autowired
    private AllUsersRepo allUsersRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String createAccount(AllUsersDTO allUsersDTO) {

        AllUsers allUsers = new AllUsers(
                allUsersDTO.getName(),
                allUsersDTO.getEmail(),
                this.passwordEncoder.encode(allUsersDTO.getPassword()),
                allUsersDTO.getRole()
                );

        allUsersRepo.save(allUsers);

        return "success";

    }
}
