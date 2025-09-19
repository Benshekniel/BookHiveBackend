package service.User.impl;

import model.dto.UserBooksDTO;
import model.entity.UserBooks;
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
                userBooksDTO.getForBidding(),   // startingBidAmount
                userBooksDTO.getBiddingStartDate(),  // biddingStartDate
                userBooksDTO.getBiddingEndDate(),    // biddingEndDate
                userBooksDTO.getInitialBidPrice(),   // initialBidPrice
                userBooksDTO.getLendingAmount(),     // lendingAmount
                userBooksDTO.getLendingPeriod(),     // lendingPeriod
                userBooksDTO.getForExchange(),       // forExchange
                userBooksDTO.getExchangePeriod(),    // exchangePeriod
                userBooksDTO.getDescription(),       // description
                userBooksDTO.getLocation(),          // location
                userBooksDTO.getPublishYear(),       // publishYear
                userBooksDTO.getIsbn(),              // isbn
                userBooksDTO.getLanguage(),          // language
                userBooksDTO.getHashtags(),           // hashtags
                userBooksDTO.getBookImage()
        );

        userBooksRepo.save(userBooks);
        return "success";
    }

    @Override
    public List<UserBooksDTO> getAllBooks() {
        return userBooksRepo.findAll().stream()
                .map(book -> new UserBooksDTO(
                        book.getBookId(),
                        book.getUserEmail(),
                        book.getTitle(),
                        book.getAuthors(),
                        book.getGenres(),
                        book.getCondition(),
                        book.getForSale(),
                        book.getPrice(),
                        book.getForLend(),
                        book.getForBidding(),
                        book.getBiddingStartDate(),
                        book.getBiddingEndDate(),
                        book.getInitialBidPrice(),
                        book.getLendingAmount(),
                        book.getLendingPeriod(),
                        book.getForExchange(),
                        book.getExchangePeriod(),
                        book.getDescription(),
                        book.getLocation(),
                        book.getPublishYear(),
                        book.getIsbn(),
                        book.getLanguage(),
                        book.getHashtags(),
                        book.getBookImage()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public UserBooksDTO getBookById(Long id) {
        return userBooksRepo.findById(id)
                .map(book -> new UserBooksDTO(
                        book.getBookId(),
                        book.getUserEmail(),
                        book.getTitle(),
                        book.getAuthors(),
                        book.getGenres(),
                        book.getCondition(),
                        book.getForSale(),
                        book.getPrice(),
                        book.getForLend(),
                        book.getForBidding(),
                        book.getBiddingStartDate(),
                        book.getBiddingEndDate(),
                        book.getInitialBidPrice(),
                        book.getLendingAmount(),
                        book.getLendingPeriod(),
                        book.getForExchange(),
                        book.getExchangePeriod(),
                        book.getDescription(),
                        book.getLocation(),
                        book.getPublishYear(),
                        book.getIsbn(),
                        book.getLanguage(),
                        book.getHashtags(),
                        book.getBookImage()
                ))
                .orElse(null);
    }

    @Override
    public String updateBook(Long id, UserBooksDTO userBooksDTO) {
        return userBooksRepo.findById(id).map(book -> {
            book.setTitle(userBooksDTO.getTitle());
            book.setAuthors(userBooksDTO.getAuthors());
            book.setGenres(userBooksDTO.getGenres());
            book.setCondition(userBooksDTO.getCondition());
            book.setForSale(userBooksDTO.getForSale());
            book.setPrice(userBooksDTO.getPrice());
            book.setForLend(userBooksDTO.getForLend());
            book.setForBidding(userBooksDTO.getForBidding());
            book.setBiddingStartDate(userBooksDTO.getBiddingStartDate());
            book.setBiddingEndDate(userBooksDTO.getBiddingEndDate());
            book.setInitialBidPrice(userBooksDTO.getInitialBidPrice());
            book.setLendingAmount(userBooksDTO.getLendingAmount());
            book.setLendingPeriod(userBooksDTO.getLendingPeriod());
            book.setForExchange(userBooksDTO.getForExchange());
            book.setExchangePeriod(userBooksDTO.getExchangePeriod());
            book.setDescription(userBooksDTO.getDescription());
            book.setLocation(userBooksDTO.getLocation());
            book.setPublishYear(userBooksDTO.getPublishYear());
            book.setIsbn(userBooksDTO.getIsbn());
            book.setLanguage(userBooksDTO.getLanguage());
            book.setHashtags(userBooksDTO.getHashtags());
            book.setBookImage(userBooksDTO.getBookImage());
            userBooksRepo.save(book);
            return "success";
        }).orElse("book not found");
    }

    @Override
    public String deleteBook(Long id) {
        if (userBooksRepo.existsById(id)) {
            userBooksRepo.deleteById(id);
            return "success";
        }
        return "book not found";
    }
}