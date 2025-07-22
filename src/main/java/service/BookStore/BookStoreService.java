package service.BookStore;

import model.dto.BookStore.BookStoreDTO.*;
import java.util.List;
import java.util.Optional;

public interface BookStoreService {

    BookStoreResponseDTO createBookStore(BookStoreCreateDTO createDTO, Long userId);

//    List<BookStoreListDTO> getAllBookStores();

//    Optional<BookStorePublicDTO> getBookStoreById(Integer storeId);
//
//    Optional<BookStoreResponseDTO> getBookStoreByUserId(Long userId);

    BookStoreResponseDTO updateBookStore(BookStoreUpdateDTO updateDTO, Long userId);

    void deleteBookStore(Integer storeId, Long userId);

//    List<BookStoreListDTO> searchBookStores(String query);

//    List<BookStoreListDTO> getBookStoresByType(String booksType);
}