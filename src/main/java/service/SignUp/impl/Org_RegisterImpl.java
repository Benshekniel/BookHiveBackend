package service.SignUp.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.dto.OrgDTO;
import model.entity.AllUsers;
import model.entity.Organization;
import model.repo.AllUsersRepo;
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid organization data provided");
        }

        // Check if email already exists in all users
        AllUsers existingUser = allUsersRepo.findByEmail(orgDTO.getEmail());
        if (existingUser != null) {
            log.warn("Email already in use: {}", orgDTO.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use: " + orgDTO.getEmail());
        }

        // Check if reg_no already exists in organizations
        Optional<Organization> existingOrg = organizationRepository.findByRegNo(orgDTO.getReg_no());
        if (existingOrg.isPresent()) {
            log.warn("Registration number already in use: {}", orgDTO.getReg_no());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Registration number already in use: " + orgDTO.getReg_no());
        }

        try {
            // Create and save new organization
            Organization organization = createOrganizationEntity(orgDTO);
            organizationRepository.save(organization);

            log.info("Successfully created new organization account for: {}", orgDTO.getEmail());
            return "success";
        } catch (Exception e) {
            log.error("Error creating organization account", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating organization account: " + e.getMessage());
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

        // Stricter email format validation
        if (!orgDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return false;
        }

        // Basic password strength check
        if (orgDTO.getPassword().length() < 8) {
            return false;
        }

        // Validate phone as a numeric string
        try {
            Integer.parseInt(orgDTO.getPhone().trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid phone number format: {}", orgDTO.getPhone());
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
        String phoneStr = orgDTO.getPhone().trim();
        int phone;
        try {
            phone = Integer.parseInt(phoneStr);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number format: " + phoneStr);
        }

        Organization organization = new Organization(
                orgDTO.getType() != null ? orgDTO.getType().trim() : null,
                orgDTO.getReg_no().trim(),
                orgDTO.getFname().trim(),
                orgDTO.getLname().trim(),
                email,
                passwordEncoder.encode(orgDTO.getPassword()),
                orgDTO.getPhone(),
                orgDTO.getYears(),
                orgDTO.getAddress() != null ? orgDTO.getAddress().trim() : null,
                orgDTO.getCity() != null ? orgDTO.getCity().trim() : null,
                orgDTO.getState() != null ? orgDTO.getState().trim() : null,
                orgDTO.getZip() != null ? orgDTO.getZip().trim() : null,
                orgDTO.getImageFileName(),
                orgDTO.getFileType()
        );

        return organization;
    }
}