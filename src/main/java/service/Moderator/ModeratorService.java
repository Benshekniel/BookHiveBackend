package service.Moderator;

import model.dto.AllUsersDTO;
import model.dto.UserBooksDTO;

import java.util.List;
import java.util.Map;

public interface ModeratorService {

    List<Map<String, Object>> getAllPending();


}

