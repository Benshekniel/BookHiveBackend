package service.BookStore;

import model.dto.BookStore.BSForUserDTOs;

import java.util.List;

public interface BSForUsersService {

    List<BSForUserDTOs.FullInventoryDTO> getBSInventory ();
    List<BSForUserDTOs.FullBookDTO> getBSBooks ();

//    UserBooksDTO convertInventoryToUserBook (BSInventory inventory);
//    UserBooksDTO convertBookToUserBook (BSBook book);
}
