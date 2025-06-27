package Login.service.impl;

import Login.dto.AllUsersDTO;
import Login.entity.AllUsers;
import Login.repo.AllUsersRepo;
import Login.service.RegisterAccount;
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
                allUsersDTO.getEmail(),
                this.passwordEncoder.encode(allUsersDTO.getPassword()),
                allUsersDTO.getRole()
                );

        allUsersRepo.save(allUsers);

        return "success";

    }
}
