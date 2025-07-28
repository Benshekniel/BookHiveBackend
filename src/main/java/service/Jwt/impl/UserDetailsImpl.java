package service.Jwt.impl;

import model.entity.AllUsers;
import model.repo.AllUsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsImpl implements UserDetailsService {


    @Autowired
    private AllUsersRepo allUsersRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AllUsers allUsers = allUsersRepo.findByEmail(email);
        if (allUsers == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");
        }

        return new UserPrincipal(allUsers);
    }
}
