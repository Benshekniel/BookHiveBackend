package service.SignUp.impl;

import model.dto.OrgDTO;
import model.entity.AllUsers;
import model.entity.Organization;
import model.repo.AllUsersRepo;
import model.repo.OrgRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import service.SignUp.Register_OrgAccount;

import java.util.Optional;

@Service
public class Org_RegisterImpl implements Register_OrgAccount {

    @Autowired
    private OrgRepo orgRepo;

    @Autowired
    private AllUsersRepo allUsersRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String createOrg(OrgDTO orgDTO) {
        try {
            // Check if email already exists in all users
            AllUsers allUsers = allUsersRepo.findByEmail(orgDTO.getEmail());
            if (allUsers != null) {
                return "email already in use";
            }

            // Check if reg_no already exists in organizations
            Optional<Organization> allOrgs = orgRepo.findByRegNo(orgDTO.getReg_no());
            if (allOrgs.isPresent()) {
                return "registration number already in use";
            }

            // Convert string values to appropriate types where needed
            Long yearsLong = null;
            if (orgDTO.getYears() != 0) {
                yearsLong = (long) orgDTO.getYears();
            }

            // First create entry in AllUsers table
            AllUsers newUser = new AllUsers();
            newUser.setEmail(orgDTO.getEmail());
            newUser.setPassword(this.passwordEncoder.encode(orgDTO.getPassword()));
            newUser.setName(orgDTO.getFname() + " " + orgDTO.getLname());
            newUser.setRole("ORGANIZATION");
            newUser.setPhone(orgDTO.getPhone());
            newUser.setAddress(orgDTO.getAddress());
            newUser.setCity(orgDTO.getCity());
            newUser.setState(orgDTO.getState());
            newUser.setZip(orgDTO.getZip());

            // Save user first to get the ID
            AllUsers savedUser = allUsersRepo.save(newUser);
            
            // Create organization with the user ID
            Organization organization = new Organization(
                    orgDTO.getType(),
                    orgDTO.getReg_no(),
                    orgDTO.getFname(),
                    orgDTO.getLname(),
                    orgDTO.getEmail(),
                    this.passwordEncoder.encode(orgDTO.getPassword()),
                    orgDTO.getPhone(),
                    yearsLong, // Converted to Long
                    orgDTO.getAddress(),
                    orgDTO.getCity(),
                    orgDTO.getState(),
                    orgDTO.getZip(),
                    orgDTO.getImageFileName(),
                    orgDTO.getFileType()
            );
            
            // Correctly convert the user ID to Long
            organization.setUserId(Long.valueOf(savedUser.getUser_id()));
            
            // Save the organization
            orgRepo.save(organization);

            return "success";

        } catch (Exception e) {
            e.printStackTrace(); // Log the full exception stack trace
            return "Error creating organization: " + e.getMessage();
        }
    }
}