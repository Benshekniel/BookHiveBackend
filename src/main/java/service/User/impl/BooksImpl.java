package service.User.impl;

import model.dto.BooksDTO;
import model.dto.UserBooksDTO;
import model.entity.AllUsers;
import model.entity.Books;
import model.entity.UserBooks;
import model.repo.BooksRepo;
import model.repo.OrgRepo;
import model.repo.UserBooksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.User.BooksService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BooksImpl  implements BooksService {

    @Autowired
    private UserBooksRepo userBooksRepo;

    @Override
    public String addBook(UserBooksDTO userBooksDTO) {

        // Check if the isbn already exists in books
        UserBooks bookCheck = userBooksRepo.findByIsbn(userBooksDTO.getIsbn());
        if (bookCheck != null) {
            return "isbn already in use";
        }

        UserBooks userBooks = new UserBooks(
                userBooksDTO.getUserEmail(),         // userEmail
                userBooksDTO.getTitle(),             // title
                userBooksDTO.getAuthors(),           // authors
                userBooksDTO.getGenres(),            // genres
                userBooksDTO.getCondition(),         // condition
                userBooksDTO.getForSale(),           // forSale
                userBooksDTO.getPrice(),             // price
                userBooksDTO.getForLend(),           // forLend
                userBooksDTO.getLendingAmount(),     // lendingAmount
                userBooksDTO.getLendingPeriod(),     // lendingPeriod
                userBooksDTO.getForExchange(),       // forExchange
                userBooksDTO.getExchangePeriod(),    // exchangePeriod
                userBooksDTO.getDescription(),       // description
                userBooksDTO.getLocation(),          // location
                userBooksDTO.getPublishYear(),       // publishYear
                userBooksDTO.getIsbn(),              // isbn
                userBooksDTO.getLanguage(),          // language
                userBooksDTO.getHashtags()           // hashtags
        );

        userBooksRepo.save(userBooks);
        return "success";
    }

    @Override
    public List<UserBooksDTO> getAllBooks() {
        return userBooksRepo.findAll().stream()
                .map(book -> new UserBooksDTO(
                        book.getUserEmail(),
                        book.getTitle(),
                        book.getAuthors(),
                        book.getGenres(),
                        book.getCondition(),
                        book.getForSale(),
                        book.getPrice(),
                        book.getForLend(),
                        book.getLendingAmount(),
                        book.getLendingPeriod(),
                        book.getForExchange(),
                        book.getExchangePeriod(),
                        book.getDescription(),
                        book.getLocation(),
                        book.getPublishYear(),
                        book.getIsbn(),
                        book.getLanguage(),
                        book.getHashtags()
                ))
                .collect(Collectors.toList());
    }
}