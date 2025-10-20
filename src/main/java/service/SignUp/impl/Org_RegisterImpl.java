package service.SignUp.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.dto.OrgDTO;
import model.dto.organizationNew.OrganizationNewDTO;
import model.entity.AllUsers;
import model.entity.Organization;
import model.repo.AllUsersRepo;
import model.repo.OrgRepo;
import model.repo.organization.OrganizationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import service.SignUp.Register_OrgAccount;

import java.util.Optional;

/**
 * Implementation of the organization registration service.
 * Handles creating new organization accounts after validating their information.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class Org_RegisterImpl implements Register_OrgAccount {

    private final OrganizationRepository organizationRepository;
    private final AllUsersRepo allUsersRepo;
    private final PasswordEncoder passwordEncoder;
    private final OrgRepo orgRepo;

    /**
     * Creates a new organization account using the new DTO format.
     *
     * @param orgDTO The organization data transfer object containing registration information
     * @return A status message indicating the result of the registration attempt
     */
    @Override
    @Transactional
    public String newCreateOrg(OrganizationNewDTO orgDTO) {
        log.info("Processing organization registration request for: {}", orgDTO.getEmail());

        try {
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

            organizationRepository.save(organization);
            log.info("Successfully created new organization account for: {}", orgDTO.getEmail());
            return "success";
        } catch (Exception e) {
            log.error("Error creating organization account", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating organization account: " + e.getMessage());
        }
    }

    /**
     * Updates the organization ID by email after the organization is created in AllUsers table.
     *
     * @param email The email of the organization
     */
    @Override
    @Transactional
    public void updateOrganizationIdByEmail(String email) {
        Integer userId = orgRepo.findUserIdByEmailinAllUsers(email);

        if (userId == null) {
            log.warn("No user found with email: {}", email);
            return;
        }

        int updated = orgRepo.updateUserIdByEmail(userId, email);

        if (updated > 0) {
            log.info("Updated org_id in organizations for email: {}", email);
        } else {
            log.warn("No matching record in organizations for email: {}", email);
        }
    }
}
