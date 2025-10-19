package service.SignUp.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.dto.OrgDTO;
import model.entity.AllUsers;
import model.entity.Organization;
import model.repo.AllUsersRepo;
import model.repo.OrgRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.SignUp.Register_OrgAccount;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementation of the organization registration service.
 * Handles creating new organization accounts after validating their information.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class Org_RegisterImpl implements Register_OrgAccount {

    private final OrgRepo orgRepo;
    private final AllUsersRepo allUsersRepo;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new organization account after validating the provided information.
     *
     * @param orgDTO The organization data transfer object containing registration information
     * @return A status message indicating the result of the registration attempt
     */
    @Override
    @Transactional
    public String createOrg(OrgDTO orgDTO) {
        log.info("Processing organization registration request for: {}", orgDTO.getEmail());

        if (!isValidInput(orgDTO)) {
            log.warn("Invalid organization data provided");
            return "invalid data";
        }

        try {
            // Check if email already exists in all users
            AllUsers existingUser = allUsersRepo.findByEmail(orgDTO.getEmail());
            if (existingUser != null) {
                log.warn("Email already in use: {}", orgDTO.getEmail());
                return "email already in use";
            }

            // Check if reg_no already exists in organizations
            Optional<Organization> existingOrg = orgRepo.findByRegNo(orgDTO.getReg_no());
            if (existingOrg.isPresent()) {
                log.warn("Registration number already in use: {}", orgDTO.getReg_no());
                return "registration number already in use";
            }

            // If both checks pass, create and save new organization
            Organization organization = createOrganizationEntity(orgDTO);
            orgRepo.save(organization);

            log.info("Successfully created new organization account for: {}", orgDTO.getEmail());
            return "success";
        } catch (Exception e) {
            log.error("Error creating organization account", e);
            return "error: " + e.getMessage();
        }
    }

    /**
     * Validates the organization input data.
     *
     * @param orgDTO The organization data to validate
     * @return true if data is valid, false otherwise
     */
    private boolean isValidInput(OrgDTO orgDTO) {
        // Check for null or empty required fields
        if (orgDTO.getEmail() == null || orgDTO.getEmail().trim().isEmpty() ||
                orgDTO.getReg_no() == null || orgDTO.getReg_no().trim().isEmpty() ||
                orgDTO.getPassword() == null || orgDTO.getPassword().trim().isEmpty() ||
                orgDTO.getFname() == null || orgDTO.getFname().trim().isEmpty() ||
                orgDTO.getLname() == null || orgDTO.getLname().trim().isEmpty() ||
                orgDTO.getPhone() == null || orgDTO.getPhone().trim().isEmpty()) {
            return false;
        }

        // Basic email format validation
        if (!orgDTO.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return false;
        }

        // Basic password strength check
        if (orgDTO.getPassword().length() < 8) {
            return false;
        }

        return true;
    }

    /**
     * Creates an Organization entity from the provided DTO.
     *
     * @param orgDTO The data transfer object with organization information
     * @return A new Organization entity
     */
    private Organization createOrganizationEntity(OrgDTO orgDTO) {
        // Normalize data
        String email = orgDTO.getEmail().toLowerCase().trim();

        Organization organization = new Organization(
                orgDTO.getType(),
                orgDTO.getReg_no().trim(),
                orgDTO.getFname().trim(),
                orgDTO.getLname().trim(),
                email,
                passwordEncoder.encode(orgDTO.getPassword()),
                07454654654,
                orgDTO.getYears(),
                orgDTO.getAddress() != null ? orgDTO.getAddress().trim() : null,
                orgDTO.getCity() != null ? orgDTO.getCity().trim() : null,
                orgDTO.getState() != null ? orgDTO.getState().trim() : null,
                orgDTO.getZip() != null ? orgDTO.getZip().trim() : null,
                orgDTO.getImageFileName(),
                orgDTO.getFileType()
        );

        // Set creation timestamp if your entity has such a field
        // organization.setCreatedAt(LocalDateTime.now());

        return organization;
    }
}