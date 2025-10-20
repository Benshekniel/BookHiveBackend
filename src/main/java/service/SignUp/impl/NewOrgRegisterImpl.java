package service.SignUp.impl;


import model.dto.BookStore.NewBookStoreDTO;
import model.dto.organizationNew.OrganizationNewDTO;
import model.entity.AllUsers;
import model.entity.BookStore;
import model.entity.Organization;
import model.repo.AllUsersRepo;
import model.repo.ModeratorRepo;
import model.repo.OrgRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import service.Register.RegisterAccount;
import service.SignUp.Register_OrgAccount;

@Service
@Primary
public class NewOrgRegisterImpl implements Register_OrgAccount {

    @Autowired
    private OrgRepo orgRepo;

    @Override
    public String newCreateOrg(OrganizationNewDTO orgDTO) {
            // If both checks pass, save new organization
            Organization organization = new Organization(
                    orgDTO.getFname(),
                    orgDTO.getLname(),
                    orgDTO.getEmail(),
                    orgDTO.getPassword(),
                    orgDTO.getPhone(),
                    orgDTO.getRegNo(),
                    orgDTO.getYears(),
                    orgDTO.getAddress(),
                    orgDTO.getCity(),
                    orgDTO.getState(),
                    orgDTO.getZip(),
                    orgDTO.getImageFileName(),
                    orgDTO.getType()
            );

            orgRepo.save(organization);
            return "success";
        }


    @Override
    public void updateOrganizationIdByEmail(String email) {
        Integer userId = orgRepo.findUserIdByEmailinAllUsers(email);

        if (userId == null) {
            System.out.println("⚠️ No user found with email: " + email);
            return;
        }

        int updated = orgRepo.updateUserIdByEmail(userId, email);

        if (updated > 0) {
            System.out.println("✅ Updated user_id in BookStore for email: " + email);
        } else {
            System.out.println("⚠️ No matching record in BookStore for email: " + email);
        }
    }
}
