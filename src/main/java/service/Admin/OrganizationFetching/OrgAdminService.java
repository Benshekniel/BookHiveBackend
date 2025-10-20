package service.Admin.OrganizationFetching;

import model.entity.Organization;

import java.util.List;

public interface OrgAdminService {

    public List<Organization> getActiveOrganizations();

    public List<Organization> getBannedOrganizations();

    public List<Organization> getRejectedOrganizations();

    public List<Organization> getPendingOrganizations();

    String activateOrganization(Integer userId);
    String banOrganization(Integer userId);
    String rejectOrganization(Integer userId);
}
