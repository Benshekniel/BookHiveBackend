package service.SignUp;

import model.dto.OrgDTO;
import model.dto.organizationNew.OrganizationNewDTO;

public interface Register_OrgAccount {

//    String createOrg(OrgDTO orgDTO);

    String newCreateOrg(OrganizationNewDTO orgDTO);

    public void updateOrganizationIdByEmail(String email);
}
