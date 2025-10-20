package service.SignUp.impl;


import model.dto.BookStore.NewBookStoreDTO;
import model.dto.OrgDTO;
import model.dto.UsersDto;
import model.entity.AllUsers;
import model.entity.BookStore;
import model.entity.Organization;
import model.entity.Users;
import model.repo.AllUsersRepo;
import model.repo.BookStore.BookStoreRepo;
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
    private BookStoreRepo bookStoreRepo;

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

    @Override
    public String createBookStore(NewBookStoreDTO newBookStoreDTO) {
        // Check if email already exists in all users
        AllUsers allUsers = allUsersRepo.findByEmail(newBookStoreDTO.getEmail());
        if (allUsers != null) {
            return "email already in use";
        }

        // If both checks pass, save new organization
        BookStore bookStore = new BookStore(
                newBookStoreDTO.getfName(),
                newBookStoreDTO.getlName(),
                newBookStoreDTO.getEmail(),
                newBookStoreDTO.getPhoneNumber(),
                newBookStoreDTO.getAddress(),
                newBookStoreDTO.getCity(),
                newBookStoreDTO.getDistrict(),
                newBookStoreDTO.getPostalCode(),
                newBookStoreDTO.getStoreName(),
                newBookStoreDTO.getBusinessRegistrationNumber(),
                newBookStoreDTO.getEsblishedYears(),
                newBookStoreDTO.getRegistryImage(),
                "PENDING"
        );

        bookStoreRepo.save(bookStore);
        return "success";
    }
    @Override
    public void updateBookStoreIdByEmail(String email) {
        Integer userId = bookStoreRepo.findUserIdByEmailinAllUsers(email);

        if (userId == null) {
            System.out.println("⚠️ No user found with email: " + email);
            return;
        }

        int updated = bookStoreRepo.updateUserIdByEmail(userId, email);

        if (updated > 0) {
            System.out.println("✅ Updated user_id in BookStore for email: " + email);
        } else {
            System.out.println("⚠️ No matching record in BookStore for email: " + email);
        }
    }
}
