package service.BookStore.Impl;

import model.dto.BookStore.BSStatDTOs;
import model.entity.AllUsers;
import model.entity.BookStore;
import model.repo.AllUsersRepo;
import model.repo.BookStore.BSBookRepo;
import model.repo.BookStore.BSInventoryRepo;
import model.repo.BookStore.BookStoreRepo;
import model.dto.BookStore.BookStoreDTOs;
import service.BookStore.BookStoreService;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

@Service @Transactional
@RequiredArgsConstructor
public class BookStoreServiceImpl implements BookStoreService {

    private final BookStoreRepo bookStoreRepo;
    private final AllUsersRepo allUsersRepo;

    private final BSInventoryRepo inventoryRepo;
    private final BSBookRepo bookRepo;

    // Common mapper resource for the entire service class:
    private static final ModelMapper modelMapper = new ModelMapper();

    public Integer getStoreIdByUserId(Integer userId) {

        BookStore bookStore = bookStoreRepo.findByAllUserNew(userId);
        return bookStore.getStoreId();
    }

    public boolean updateBookStore (Integer userId, BookStoreDTOs.ProfileBookStoreDTO bookStoreDTO) {
        Integer storeId = getStoreIdByUserId(userId);
        return bookStoreRepo.findByStoreId(storeId)
                .map(existingBookStore -> {
                    modelMapper.map(bookStoreDTO, existingBookStore);
                    bookStoreRepo.save(existingBookStore);
                    return true;
                })
                .orElse(false);
    }

    public boolean changePassword (BookStoreDTOs.PassChangeDTO passChangeDTO) {
        Optional<AllUsers> bsUserOpt = allUsersRepo.findById(passChangeDTO.getUserId());
        if (bsUserOpt.isEmpty())
            return false;
        else {
            AllUsers bsUser = bsUserOpt.get();
            if (passChangeDTO.getOldPassword().equals(bsUser.getPassword())) {
                bsUser.setPassword(passChangeDTO.getNewPassword());
                allUsersRepo.save(bsUser);
                return true;
            }
            else return false;
        }
    }

    public BookStoreDTOs.ProfileBookStoreDTO getStoreProfileDetails (Integer userId) {
        Integer storeId = getStoreIdByUserId(userId);

        Optional<BookStore> storeOpt = bookStoreRepo.findByStoreId (storeId);
        if (storeOpt.isEmpty())
            return null;
        else {
            BookStore store = storeOpt.get();
            return modelMapper.map(store, BookStoreDTOs.ProfileBookStoreDTO.class);
        }
    }

    public BSStatDTOs.DashboardStatsDTO getBookStoreStats (Integer userId) {
        Integer storeId = getStoreIdByUserId(userId);

        BSStatDTOs.DashboardStatsDTO statDTO = new BSStatDTOs.DashboardStatsDTO();
        statDTO.setTotalInventoryItems(inventoryRepo.countByBookStore_StoreId(storeId));
        statDTO.setTotalBooksItems(bookRepo.countByBookStore_StoreId(storeId));
        statDTO.setTotalTransactions(0);
        statDTO.setTotalTransactionsValue(BigDecimal.valueOf(0));

        return statDTO;
    }
}
