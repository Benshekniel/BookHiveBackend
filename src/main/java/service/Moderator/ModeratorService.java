package service.Moderator;

import model.dto.AllUsersDTO;
import model.dto.UserBooksDTO;

import java.util.List;
import java.util.Map;

public interface ModeratorService {

    List<Map<String, Object>> getAllPending();

    String approveUserStatus(String email,String name);

    String rejectUserStatus(String email,String reason,String name);


}

