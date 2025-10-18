package service.BookStore;

import model.dto.BookStore.BSStatDTOs;
import model.dto.BookStore.BookStoreDTOs;

public interface BookStoreService {

    public Integer getStoreIdByUserId(Integer userId);

    public boolean registerBookStore (BookStoreDTOs.RegisterBookStoreDTO bookStoreDTO);
    public boolean updateBookStore (Integer userId, BookStoreDTOs.ProfileBookStoreDTO bookStoreDTO);

    public BookStoreDTOs.ProfileBookStoreDTO getStoreProfileDetails (Integer userId);

    BSStatDTOs.DashboardStatsDTO getBookStoreStats(Integer userId);
}
