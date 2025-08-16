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
        try {
            AllUsers allUsers = new AllUsers(
                    allUsersDTO.getName(),
                    allUsersDTO.getEmail(),
                    this.passwordEncoder.encode(allUsersDTO.getPassword()),
                    allUsersDTO.getRole(),
                    allUsersDTO.getStatus()
            );

            AllUsers savedUser = allUsersRepo.save(allUsers);

            if (savedUser.getStatus() != null) {
                return "success&" + savedUser.getStatus().name();
            } else {
                return "success&error"; // status was null
            }

        } catch (Exception e) {
            return "error&" + e.getMessage();
        }
    }

//    @Override
//    public String createAccount(AllUsersDTO allUsersDTO) {
//
//        AllUsers allUsers = new AllUsers(
//                allUsersDTO.getName(),
//                allUsersDTO.getEmail(),
//                this.passwordEncoder.encode(allUsersDTO.getPassword()),
//                allUsersDTO.getRole(),
//                allUsersDTO.getStatus()
//                );
//
//        allUsersRepo.save(allUsers);
//
//        return "success";
//
//    }
}
