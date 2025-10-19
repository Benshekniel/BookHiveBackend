package service.BookStore;

import model.dto.BookStore.BookStoreDTOs.RegisterBookStoreDTO;
import model.dto.BookStore.BookStoreDTOs.ProfileBookStoreDTO;
import model.entity.AllUsers;
import model.entity.BookStore;
import model.repo.BookStoreRepo;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@Service
@RequiredArgsConstructor
@Transactional
public class BookStoreService {

    private final BookStoreRepo bookStoreRepo;

    // Common mapper resource for the entire service class:
    private static final ModelMapper modelMapper = new ModelMapper();

    /**
     * @param userId  an AllUsers entity user_id
     * @return storeId - the corresponding bookstore's id
     */
    public Integer getStoreIdByUserId(Integer userId) {
        AllUsers bsUser = new AllUsers();
        bsUser.setUser_id(userId);

        BookStore bookStore = bookStoreRepo.findByAllUser(bsUser);
        return bookStore.getStoreId();
    }

    public boolean registerBookStore (RegisterBookStoreDTO bookStoreDTO) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        BookStore bookStore = modelMapper.map(bookStoreDTO, BookStore.class);
            AllUsers user = new AllUsers();
            user.setUser_id(bookStoreDTO.getUserId());
            bookStore.setAllUser(user);

        bookStoreRepo.save(bookStore);
        return true;
    }

    public boolean updateBookStore (Integer userId, ProfileBookStoreDTO bookStoreDTO) {
        Integer storeId = getStoreIdByUserId(userId);
        return bookStoreRepo.findByStoreId(storeId)
                .map(existingBookStore -> {
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                    modelMapper.map(bookStoreDTO, existingBookStore);
                    bookStoreRepo.save(existingBookStore);

                    return true;
                })
                .orElse(false);
    }

//    public ResponseEntity<ProfileBookStoreDTO> getBookStoreById (Integer id) {
//        return bookStoreRepo.findByStoreId(id)
//                .map(existingBookStore -> {
//                    modelMapper.getConfiguration().setSkipNullEnabled(true);
//                    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//                    ProfileBookStoreDTO bookStoreProfile = modelMapper.map(existingBookStore, ProfileBookStoreDTO.class);
//                    return ResponseEntity.ok(bookStoreProfile);
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }


}
