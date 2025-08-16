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

        // If both checks pass, save new organization
        Organization organization = new Organization(
                orgDTO.getType(),
                orgDTO.getReg_no(),
                orgDTO.getFname(),
                orgDTO.getLname(),
                orgDTO.getEmail(),
                this.passwordEncoder.encode(orgDTO.getPassword()),
                orgDTO.getPhone(),
                orgDTO.getYears(),
                orgDTO.getAddress(),
                orgDTO.getCity(),
                orgDTO.getState(),
                orgDTO.getZip(),
                orgDTO.getImageFileName(),
                orgDTO.getFileType()
        );

        orgRepo.save(organization);
        return "success";
    }
}
