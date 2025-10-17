package service.Admin;

import model.dto.ModeratorDto;
import model.entity.Moderator;
import model.repo.Admin.moderatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminModeratorService {

    @Autowired
    private moderatorRepository moderatorRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ==================== CREATE OPERATIONS ====================

    // Create a new moderator
    public Moderator createModerator(ModeratorDto moderatorDto) {
        // Validate email uniqueness
        if (moderatorRepository.existsByEmail(moderatorDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new moderator entity
        Moderator moderator = new Moderator();
        moderator.setId(moderatorDto.getId());
        moderator.setName(moderatorDto.getName());
        moderator.setEmail(moderatorDto.getEmail());
        moderator.setPassword(passwordEncoder.encode(moderatorDto.getPassword()));
        moderator.setPhone(moderatorDto.getPhone());
        moderator.setDob(moderatorDto.getDob());
        moderator.setCity(moderatorDto.getCity());
        moderator.setExperience(moderatorDto.getExperience());
        moderator.setAddress(moderatorDto.getAddress());

        // Save moderator
        return moderatorRepository.save(moderator);
    }

    // ==================== READ OPERATIONS ====================

    // Get moderator by ID
    public Moderator getModeratorById(Long id) {
        Optional<Moderator> optionalModerator = moderatorRepository.findById(id);
        if (optionalModerator.isEmpty()) {
            throw new RuntimeException("Moderator not found with id: " + id);
        }
        return optionalModerator.get();
    }

    // Get moderator by ID with DTO response
    public ModeratorDto getModeratorDtoById(Long id) {
        Moderator moderator = getModeratorById(id);
        return convertToDto(moderator);
    }

    // Get all moderators
    public List<Moderator> getAllModerators() {
        return moderatorRepository.findAllModeratorsOrderedByIdDesc();
    }

    // Get all moderators as DTOs
    public List<ModeratorDto> getAllModeratorsDto() {
        List<Moderator> moderators = getAllModerators();
        return convertToDtoList(moderators); // ✅ Now this call will work
    }


    // Find moderator by email
    public Moderator findByEmail(String email) {
        Optional<Moderator> moderator = moderatorRepository.findByEmail(email);
        if (moderator.isEmpty()) {
            throw new RuntimeException("Moderator not found with email: " + email);
        }
        return moderator.get();
    }

    // Find moderator by email with DTO response
    public ModeratorDto findByEmailDto(String email) {
        Moderator moderator = findByEmail(email);
        return convertToDto(moderator);
    }




    // Search moderators by name or email
    public List<Moderator> searchModerators(String searchTerm) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return moderatorRepository.searchByNameOrEmail(searchTerm);
        } else {
            return moderatorRepository.findAllModeratorsOrderedByIdDesc();
        }
    }

    // Search moderators with DTO response
    public List<ModeratorDto> searchModeratorsDto(String searchTerm) {
        List<Moderator> moderators = searchModerators(searchTerm);
        return convertToDtoList(moderators);
    }

    // Get moderators by city
    public List<Moderator> getModeratorsByCity(String city) {
        return moderatorRepository.findByCity(city);
    }

    // Get moderators by city with DTO response
    public List<ModeratorDto> getModeratorsByCityDto(String city) {
        List<Moderator> moderators = getModeratorsByCity(city);
        return convertToDtoList(moderators);
    }

    // Get moderators by experience level
    public List<Moderator> getModeratorsByExperience(Integer minExperience) {
        return moderatorRepository.findByExperienceGreaterThanEqual(minExperience);
    }

    // Get moderators by experience level with DTO response
    public List<ModeratorDto> getModeratorsByExperienceDto(Integer minExperience) {
        List<Moderator> moderators = getModeratorsByExperience(minExperience);
        return convertToDtoList(moderators);
    }

    // Get moderators by age range (calculated from DOB)
    public List<Moderator> getModeratorsByAgeRange(int minAge, int maxAge) {
        LocalDate maxBirthDate = LocalDate.now().minusYears(minAge);
        LocalDate minBirthDate = LocalDate.now().minusYears(maxAge + 1);

        return moderatorRepository.findAll().stream()
                .filter(m -> m.getDob() != null)
                .filter(m -> m.getDob().isAfter(minBirthDate) && m.getDob().isBefore(maxBirthDate))
                .collect(Collectors.toList());
    }

    // Get moderators count
    public long getModeratorCount() {
        return moderatorRepository.count();
    }

    // Get moderators by name containing
    public List<Moderator> getModeratorsByNameContaining(String name) {
        return moderatorRepository.findAll().stream()
                .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Check if moderator exists by ID
    public boolean existsById(Long id) {
        return moderatorRepository.existsById(id);
    }

    // Check if moderator exists by email
    public boolean existsByEmail(String email) {
        return moderatorRepository.existsByEmail(email);
    }

    // ==================== UPDATE OPERATIONS ====================

    // Update moderator details
    public Moderator updateModerator(Long id, ModeratorDto moderatorDto) {
        // Validate DTO
        if (!moderatorDto.isValidForUpdate()) {
            throw new RuntimeException("Invalid moderator data for update");
        }

        // Trim string fields
        moderatorDto.trimStringFields();

        // Find existing moderator
        Optional<Moderator> optionalModerator = moderatorRepository.findById(id);
        if (optionalModerator.isEmpty()) {
            throw new RuntimeException("Moderator not found with id: " + id);
        }

        Moderator moderator = optionalModerator.get();

        // Validate email uniqueness (excluding current moderator)
        if (moderatorDto.getEmail() != null &&
                !moderatorDto.getEmail().equals(moderator.getEmail()) &&
                moderatorRepository.existsByEmailAndIdNot(moderatorDto.getEmail(), id)) {
            throw new RuntimeException("Email already exists");
        }

        // Update fields if provided
        if (moderatorDto.getName() != null) {
            moderator.setName(moderatorDto.getName());
        }
        if (moderatorDto.getEmail() != null) {
            moderator.setEmail(moderatorDto.getEmail());
        }
        if (moderatorDto.getPassword() != null && !moderatorDto.getPassword().trim().isEmpty()) {
            moderator.setPassword(passwordEncoder.encode(moderatorDto.getPassword()));
        }
        if (moderatorDto.getPhone() > 0) {
            moderator.setPhone(moderatorDto.getPhone());
        }
        if (moderatorDto.getDob() != null) {
            moderator.setDob(moderatorDto.getDob());
        }
        if (moderatorDto.getCity() != null) {
            moderator.setCity(moderatorDto.getCity());
        }
        if (moderatorDto.getExperience() >= 0) {
            moderator.setExperience(moderatorDto.getExperience());
        }
        if (moderatorDto.getAddress() != null) {
            moderator.setAddress(moderatorDto.getAddress());
        }

        // Save updated moderator
        return moderatorRepository.save(moderator);
    }

    // ==================== DELETE OPERATIONS ====================

    // Delete moderator by ID
    public void deleteModerator(Long id) {
        if (!moderatorRepository.existsById(id)) {
            throw new RuntimeException("Moderator not found with id: " + id);
        }
        moderatorRepository.deleteById(id);
    }

    // Delete moderator by email
    public void deleteModeratorByEmail(String email) {
        Moderator moderator = findByEmail(email);
        moderatorRepository.deleteById(moderator.getId());
    }

    // Soft delete (if you want to implement soft delete later)
    // This is a placeholder for future soft delete functionality
    public void softDeleteModerator(Long id) {
        // For now, this performs a hard delete
        // Later you can add a 'deleted' field to your entity
        deleteModerator(id);
    }

    // Delete multiple moderators by IDs
    public void deleteModerators(List<Long> ids) {
        for (Long id : ids) {
            if (moderatorRepository.existsById(id)) {
                moderatorRepository.deleteById(id);
            }
        }
    }

    // Delete all moderators by city
    public int deleteModeratorsByCity(String city) {
        List<Moderator> moderators = getModeratorsByCity(city);
        int count = moderators.size();
        for (Moderator moderator : moderators) {
            moderatorRepository.deleteById(moderator.getId());
        }
        return count;
    }

    // ==================== UTILITY METHODS ====================

    // Convert Entity to DTO (for response)
    public ModeratorDto convertToDto(Moderator moderator) {
        ModeratorDto dto = new ModeratorDto();
        dto.setId(moderator.getId());
        dto.setName(moderator.getName());
        dto.setEmail(moderator.getEmail());
        // Don't include password in response for security
        dto.setPhone(moderator.getPhone());
        dto.setDob(moderator.getDob());
        dto.setCity(moderator.getCity());
        dto.setExperience(moderator.getExperience());
        dto.setAddress(moderator.getAddress());
        return dto;
    }

    // Convert Entity list to DTO list
    public List<ModeratorDto> convertToDtoList(List<Moderator> moderators) {
        return moderators.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Convert Entity list to DTO list
//    public List<ModeratorDto> convertToDtoList(List<Moderator> moderators) {
//        return moderators.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

    // Get moderator's age from DOB
    public int getModeratorAge(Long id) {
        Moderator moderator = getModeratorById(id);
        if (moderator.getDob() != null) {
            return LocalDate.now().getYear() - moderator.getDob().getYear();
        }
        return 0;
    }

    // Validate moderator data
    public boolean validateModeratorData(ModeratorDto moderatorDto) {
        return moderatorDto.getName() != null && !moderatorDto.getName().trim().isEmpty() &&
                moderatorDto.getEmail() != null && !moderatorDto.getEmail().trim().isEmpty() &&
                moderatorDto.getPassword() != null && !moderatorDto.getPassword().trim().isEmpty();
    }

}
