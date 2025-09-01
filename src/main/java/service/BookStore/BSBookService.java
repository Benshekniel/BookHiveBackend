package service.BookStore;

import model.dto.BookStore.BSBookDTOs;
import model.dto.BookStore.BSBookDTOs.RegisterBookDTO;
import model.dto.BookStore.BSBookDTOs.UpdateBookDTO;
import model.dto.BookStore.BSBookDTOs.ViewBookDTO;
import model.entity.BSBook;
import model.entity.BookStore;
import model.repo.BSBookRepo;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BSBookService {

    // Common mapper resource for the entire service class:

    private final BookStoreService bookStoreService;
    private final BSBookRepo bookRepo;
    private static final ModelMapper modelMapper = new ModelMapper();

    public boolean registerBook (RegisterBookDTO bookDTO, Integer userId) {
        Integer storeId = bookStoreService.getStoreIdByUserId(userId);

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        BSBook book = modelMapper.map(bookDTO, BSBook.class);
            BookStore bookStore = new BookStore();
            bookStore.setStoreId(storeId);
            book.setBookStore(bookStore);
        bookRepo.save(book);
        return true;
    }

    public boolean updateBook (Integer bookId, UpdateBookDTO bookDTO) {
        return bookRepo.findByBookId(bookId)
                .map(existingBook -> {
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                    modelMapper.map(bookDTO, existingBook);
                    bookRepo.save(existingBook);

                    return true;
                })
                .orElse(false);
    }

    public ViewBookDTO getBookById (Integer bookId) {
        Optional<BSBook> bookOpt = bookRepo.findByBookId(bookId);
        if (bookOpt.isEmpty())
            return null;
        else {
            BSBook book = bookOpt.get();
            return modelMapper.map(book, ViewBookDTO.class);
        }
    }

//    public List<BSBookDTOs.BookListingDTO> getBooksByStore (Integer storeId) {
//        modelMapper.getConfiguration().setSkipNullEnabled(true);
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//        List<BSBook> bookList =  bookRepo.findByBookStore_StoreId(storeId);
//        if (bookList.isEmpty())
//            return Collections.emptyList();
//        return bookList.stream()
//                .map(book -> modelMapper.map(book, BSBookDTOs.BookListingDTO.class))
//                .toList();
//    }

}
