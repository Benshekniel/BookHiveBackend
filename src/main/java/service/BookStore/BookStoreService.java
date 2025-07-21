package service.BookStore;

import model.dto.BookStore.BookStoreDTO.*;
import java.util.List;
import java.util.Optional;

public interface BookStoreService {

    BookStoreResponseDTO createBookStore(BookStoreCreateDTO createDTO, Integer userId);

    List<BookStoreListDTO> getAllBookStores();

    Optional<BookStorePublicDTO> getBookStoreById(Integer storeId);

    Optional<BookStoreResponseDTO> getBookStoreByUserId(Integer userId);

    BookStoreResponseDTO updateBookStore(BookStoreUpdateDTO updateDTO, Integer userId);

    void deleteBookStore(Integer storeId, Integer userId);

    List<BookStoreListDTO> searchBookStores(String query);

    List<BookStoreListDTO> getBookStoresByType(String booksType);
}