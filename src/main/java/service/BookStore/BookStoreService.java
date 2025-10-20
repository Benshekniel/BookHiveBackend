package service.BookStore;

import model.dto.BookStore.BSStatDTOs;
import model.dto.BookStore.BookStoreDTOs;

public interface BookStoreService {

    Integer getStoreIdByUserId(Integer userId);

    boolean updateBookStore (Integer userId, BookStoreDTOs.ProfileBookStoreDTO bookStoreDTO);
    boolean changePassword (BookStoreDTOs.PassChangeDTO passChangeDTO);

    BookStoreDTOs.ProfileBookStoreDTO getStoreProfileDetails (Integer userId);

    BSStatDTOs.DashboardStatsDTO getBookStoreStats(Integer userId);
    boolean updateDonatedCount (Integer storeId, Integer addition);
}
