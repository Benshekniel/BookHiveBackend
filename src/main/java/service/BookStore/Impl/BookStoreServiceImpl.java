package service.BookStore.Impl;

import model.entity.AllUsers;
import model.entity.BookStore;
import model.repo.BookStore.BookStoreRepo;
import model.dto.BookStore.BookStoreDTOs;
import service.BookStore.BookStoreService;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;

@Service @Transactional
@RequiredArgsConstructor
public class BookStoreServiceImpl implements BookStoreService {

    private final BookStoreRepo bookStoreRepo;
    // Common mapper resource for the entire service class:
    private static final ModelMapper modelMapper = new ModelMapper();

    public Integer getStoreIdByUserId(Integer userId) {
//        AllUsers bsUser = new AllUsers();
//        bsUser.setUser_id(userId);

        BookStore bookStore = bookStoreRepo.findByAllUserNew(userId);
        return bookStore.getStoreId();
    }

    public boolean registerBookStore (BookStoreDTOs.RegisterBookStoreDTO bookStoreDTO) {
//        BookStore bookStore = modelMapper.map(bookStoreDTO, BookStore.class);
//        AllUsers user = new AllUsers();
//        user.setUser_id(bookStoreDTO.getUserId());
//        bookStore.setAllUser(user);
//
//        bookStoreRepo.save(bookStore);
        return true;
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
}
