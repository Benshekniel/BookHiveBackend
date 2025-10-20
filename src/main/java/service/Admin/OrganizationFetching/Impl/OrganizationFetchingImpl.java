package service.Admin.OrganizationFetching.Impl;

import jakarta.transaction.Transactional;
import model.entity.Organization;
import model.repo.Admin.AdminControlsOrganzationRepo;
import model.repo.AllUsersRepo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.Admin.OrganizationFetching.OrgAdminService;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationFetchingImpl implements OrgAdminService {

    @Autowired
    private AdminControlsOrganzationRepo adminControlsOrganzationRepo;

    @Autowired
    private AllUsersRepo allUsersRepo;

    // ✅ 1. Get all active organizations
    public List<Organization> getActiveOrganizations() {
        List<String> emails = adminControlsOrganzationRepo.findActiveOrganizationEmails();
        return emails.isEmpty() ? new ArrayList<>() : adminControlsOrganzationRepo.findOrganizationsByEmails(emails);
    }

    // ✅ 2. Get all banned organizations
    public List<Organization> getBannedOrganizations() {
        List<String> emails = adminControlsOrganzationRepo.findBannedOrganizationEmails();
        return emails.isEmpty() ? new ArrayList<>() : adminControlsOrganzationRepo.findOrganizationsByEmails(emails);
    }

    // ✅ 3. Get all rejected organizations
    public List<Organization> getRejectedOrganizations() {
        List<String> emails = adminControlsOrganzationRepo.findRejectedOrganizationEmails();
        return emails.isEmpty() ? new ArrayList<>() : adminControlsOrganzationRepo.findOrganizationsByEmails(emails);
    }

    // ✅ 3. Get all rejected organizations
    public List<Organization> getPendingOrganizations() {
        List<String> emails = adminControlsOrganzationRepo.findPendingOrganizationEmails();
        return emails.isEmpty() ? new ArrayList<>() : adminControlsOrganzationRepo.findOrganizationsByEmails(emails);
    }

    // ✅ Activate Organization
    @Override
    @Transactional
    public String activateOrganization(Integer userId) {
        int updated = adminControlsOrganzationRepo.activateOrganizationById(userId);
        return updated > 0 ? "Organization activated successfully." : "Organization not found or already active.";
    }

    // ✅ Ban Organization
    @Override
    @Transactional
    public String banOrganization(Integer userId) {
        int updated = adminControlsOrganzationRepo.banOrganizationById(userId);
        return updated > 0 ? "Organization banned successfully." : "Organization not found or already banned.";
    }

    // ✅ Reject Organization
    @Override
    @Transactional
    public String rejectOrganization(Integer userId) {
        int updated = adminControlsOrganzationRepo.rejectOrganizationById(userId);
        return updated > 0 ? "Organization rejected successfully." : "Organization not found or already rejected.";
    }
}
