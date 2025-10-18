package service.User;

//import model.dto.BooksDTO;
import model.dto.UserBooksDTO;

import java.util.List;

public interface BooksService {
    String addBook(UserBooksDTO userBooksDTO);
    List<UserBooksDTO> getAllBooks();
    UserBooksDTO getBookById(Long id);
    String updateBook(Long id, UserBooksDTO userBooksDTO);
    String deleteBook(Long id);


}