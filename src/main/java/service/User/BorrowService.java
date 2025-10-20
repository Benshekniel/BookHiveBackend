package service.User;

import model.dto.Bidding.UserBorrowRequestDTO;
import model.entity.Bid.UserBorrowRequest;

import java.util.List;

public interface BorrowService {

    UserBorrowRequestDTO createBorrowRequest(UserBorrowRequestDTO incoming);

    UserBorrowRequestDTO getById(Long requestId);

    List<UserBorrowRequestDTO> getByRequestor(int userId);

    List<UserBorrowRequestDTO> getByOwner(int ownerId);

    UserBorrowRequestDTO updateStatus(Long requestId, String status); // APPROVED/REJECTED/COMPLETED/CANCELLED

    /// ////////////////////////////////////////////////////////////
    boolean existsBorrowRequest(int userId, Long bookId);

    // Optional: active-only
    boolean existsActiveBorrowRequest(int userId, Long bookId);


/// //////////////////////////////////////////////////////////
    List<UserBorrowRequest> getRequestsByUserEntity(int userId);

    List<UserBorrowRequest> getRequestsByOwnerEntity(int ownerId);

///  /////////////////////////////////////////////////////////
    UserBorrowRequest approve(Long requestId, Long bookId, boolean autoRejectOthers);

    UserBorrowRequest reject(Long requestId, Long bookId);

    void delete(Long requestId, Long bookId);


}
