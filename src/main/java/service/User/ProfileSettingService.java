package service.User;

import model.dto.UsersDto;
import model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import model.repo.User.ProfileSettingRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProfileSettingService {

    @Autowired
    private ProfileSettingRepository profileSettingRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${file.upload.directory:uploads/avatars}")
    private String uploadDirectory;

    @Value("${file.upload.base-url:http://localhost:8080/uploads/avatars}")
    private String baseUrl;

    /**
     * Get user profile by ID
     */
    public UsersDto getUserProfile(Long userId) {
        Users user = profileSettingRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return convertToDto(user);
    }

    /**
     * Update user profile
     */
    public UsersDto updateUserProfile(Long userId, UsersDto usersDto) {
        Users user = profileSettingRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Check if email is being changed and if it's already taken
        if (usersDto.getEmail() != null && !usersDto.getEmail().equals(user.getEmail())) {
            if (!isEmailAvailable(usersDto.getEmail(), userId)) {
                throw new RuntimeException("Email is already taken");
            }
            user.setEmail(usersDto.getEmail());
        }

        // Update user fields (only non-null and non-password fields)
        if (usersDto.getName() != null && !usersDto.getName().trim().isEmpty()) {
            user.setName(usersDto.getName());
        }
        if (usersDto.getFname() != null) {
            user.setFname(usersDto.getFname());
        }
        if (usersDto.getLname() != null) {
            user.setLname(usersDto.getLname());
        }
        if (usersDto.getPhone() != 0) {
            user.setPhone(usersDto.getPhone());
        }
        if (usersDto.getGender() != null) {
            user.setGender(usersDto.getGender());
        }
        if (usersDto.getAddress() != null) {
            user.setAddress(usersDto.getAddress());
        }
        if (usersDto.getCity() != null) {
            user.setCity(usersDto.getCity());
        }
        if (usersDto.getState() != null) {
            user.setState(usersDto.getState());
        }
        if (usersDto.getZip() != null) {
            user.setZip(usersDto.getZip());
        }
        if (usersDto.getDob() != null) {
            user.setDob(usersDto.getDob());
        }
        if (usersDto.getIdType() != null) {
            user.setIdType(usersDto.getIdType());
        }

        Users updatedUser = profileSettingRepository.save(user);
        return convertToDto(updatedUser);
    }

    /**
     * Update user password
     */
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        Users user = profileSettingRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Validate new password
        if (newPassword.length() < 8) {
            throw new RuntimeException("New password must be at least 8 characters long");
        }

        // Check if new password is same as old password
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new RuntimeException("New password must be different from the old password");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        profileSettingRepository.save(user);
    }

    /**
     * Upload user avatar
     */
    public String uploadAvatar(Long userId, MultipartFile file) {
        Users user = profileSettingRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String newFilename = "avatar_" + userId + "_" + UUID.randomUUID().toString() + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Generate URL
            String avatarUrl = baseUrl + "/" + newFilename;

            // Update user's avatar URL (using idFront field as avatar storage)
            user.setIdFront(avatarUrl);
            profileSettingRepository.save(user);

            return avatarUrl;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar: " + e.getMessage());
        }
    }

    /**
     * Check if email is available
     */
    public boolean isEmailAvailable(String email, Long userIdToExclude) {
        Optional<Users> existingUser = profileSettingRepository.findByEmail(email);

        if (existingUser.isEmpty()) {
            return true;
        }

        // If user exists, check if it's the same user (updating their own profile)
        return existingUser.get().getUserId().equals(userIdToExclude);
    }

    /**
     * Convert Users entity to UsersDto
     */
    private UsersDto convertToDto(Users user) {
        UsersDto dto = new UsersDto();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setFname(user.getFname());
        dto.setLname(user.getLname());
        dto.setPhone(user.getPhone());
        dto.setDob(user.getDob());
        dto.setIdType(user.getIdType());
        dto.setIdFront(user.getIdFront()); // Avatar URL
        dto.setIdBack(user.getIdBack());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setZip(user.getZip());
        dto.setBillImage(user.getBillImage());
        // Don't include password in DTO for security
        return dto;
    }

    /**
     * Convert UsersDto to Users entity
     */
    private Users convertToEntity(UsersDto dto) {
        Users user = new Users();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setFname(dto.getFname());
        user.setLname(dto.getLname());
        user.setPhone(dto.getPhone());
        user.setDob(dto.getDob());
        user.setIdType(dto.getIdType());
        user.setIdFront(dto.getIdFront());
        user.setIdBack(dto.getIdBack());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setZip(dto.getZip());
        user.setBillImage(dto.getBillImage());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return user;
    }
}