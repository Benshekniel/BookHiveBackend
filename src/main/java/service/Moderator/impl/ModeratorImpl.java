package service.Moderator.impl;

import model.dto.AllUsersDTO;
import model.entity.AllUsers;
import model.repo.AllUsersRepo;
import model.repo.ModeratorRepo;
import service.Moderator.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModeratorImpl implements ModeratorService {

    @Autowired
    private ModeratorRepo moderatorRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AllUsersRepo allUsersRepo;



    public List<Map<String, Object>> getAllPending() {
        return allUsersRepo.findAllPending();
    }
}
