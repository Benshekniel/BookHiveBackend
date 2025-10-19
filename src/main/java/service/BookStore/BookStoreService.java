package service.BookStore;

import model.dto.BookStore.BSStatDTOs;
import model.dto.BookStore.BookStoreDTOs;

public interface BookStoreService {

    public Integer getStoreIdByUserId(Integer userId);

    public boolean updateBookStore (Integer userId, BookStoreDTOs.ProfileBookStoreDTO bookStoreDTO);
    public boolean changePassword (BookStoreDTOs.PassChangeDTO passChangeDTO);

    public BookStoreDTOs.ProfileBookStoreDTO getStoreProfileDetails (Integer userId);

    BSStatDTOs.DashboardStatsDTO getBookStoreStats(Integer userId);
}
