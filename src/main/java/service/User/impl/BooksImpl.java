package service.User.impl;

import jakarta.transaction.Transactional;
import model.dto.Bidding.UserBidDTO;
import model.dto.UserBooksDTO;
import model.entity.AllUsers;
import model.entity.Bid.Bid_History;
import model.entity.Bid.SellorMode;
import model.entity.Bid.User_Bid;
import model.entity.Delivery;
import model.entity.UserBooks;
import model.entity.Users;
import model.repo.AllUsersRepo;
import model.repo.Delivery.TransactionRepository;
import model.repo.UserBooksRepo;
import model.repo.UsersRepo;
import model.repo.bid.BidHistoryRepo;
import model.repo.bid.SellorModeRepo;
import model.repo.bid.UserBidRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.User.BooksService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BooksImpl  implements BooksService {

    @Autowired
    private UserBooksRepo userBooksRepo;

    @Autowired
    private UserBidRepo userBidRepo;

    @Autowired
    private BidHistoryRepo bidHistoryRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private AllUsersRepo allUsersRepo;

    @Autowired
    private SellorModeRepo sellorModeRepo;

    @Autowired
    private TransactionRepository transactionRepo;

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

        // Save book entry
        UserBooks savedBook = userBooksRepo.save(userBooks);


        // ✅ If "forBidding" is true, create a new Bid_History entry
        if (Boolean.TRUE.equals(userBooksDTO.getForBidding())) {
            Bid_History bid = new Bid_History();
            bid.setBookId(savedBook.getBookId()); // or savedBook.getBookId() based on your entity field
            bid.setBiddingStartDate(userBooksDTO.getBiddingStartDate());
            bid.setBiddingEndDate(userBooksDTO.getBiddingEndDate());
            bid.setInitial_bid_amount(userBooksDTO.getInitialBidPrice());
            bid.setBidEnd(false);
            bid.setBidWinner(null);
            bid.setBookImage(userBooksDTO.getBookImage());

            // Save bid entry
            bidHistoryRepo.save(bid);
        }


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

    @Override
    @Transactional
    public String updateTransactionDeliveryAddress(int allUsersId) {
        // Step 1: Get the address from Users table using All_Users user_id
        Optional<String> addressOpt = usersRepo.findAddressByAllUsersId(((int) allUsersId));
        if (addressOpt.isEmpty()) {
            return "Address not found for All_Users ID: " + allUsersId;
        }
        String address = addressOpt.get();

        // Step 2: Get the email from All_Users to find the corresponding user_id in Users
        Optional<AllUsers> allUsersOpt = allUsersRepo.findById(allUsersId);
        if (allUsersOpt.isEmpty()) {
            return "All_Users record not found for ID: " + allUsersId;
        }
        String email = allUsersOpt.get().getEmail();

        // Step 3: Get the user_id from Users table using the email
        Optional<Users> userOpt = usersRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "Users record not found for email: " + email;
        }
        Long usersTableUserId = userOpt.get().getUserId();

        // Step 4: Update transactions with the address
        int rowsAffected = usersRepo.updateAllDeliveryAddressesByUserId(((long) allUsersId), address);
        if (rowsAffected > 0) {
            return "Successfully updated " + rowsAffected + " transactions with delivery address";
        } else {
            return "No transactions found for user ID: " + usersTableUserId;
        }
    }

    @Override
    public String placeUserBid(UserBidDTO userBidDTO) {
        try {
            User_Bid userBid = new User_Bid(
                    userBidDTO.getBidId(),
                    userBidDTO.getBookId(),
                    userBidDTO.getUserId(),
                    userBidDTO.getBidAmount(),
                    userBidDTO.getName()
            );

            userBidRepo.save(userBid);
            return "Bid placed successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to place bid: " + e.getMessage();
        }
    }

    @Override
    public List<User_Bid> getBidsByBookId(Long bookId) {
        return userBidRepo.findByBookId(bookId);
    }

    // Fetch all bids for a given bookId
    @Override
    public List<Bid_History> getBidHistoryByBookId(Long bookId) {
        return bidHistoryRepo.findByBookId(bookId);
    }

    @Override
    public boolean updateBidWinner(Long bidId, String winnerName) {
        int updated = bidHistoryRepo.updateBidWinnerAndEndBid(winnerName, bidId);
        return updated > 0; // true if update successful
    }

    // Set seller mode to true for a user
    // Directly save or update a SellorMode object
    @Override
    public SellorMode setSellorMode(SellorMode sellorMode) {
        return sellorModeRepo.save(sellorMode);
    }

    @Override
    public SellorMode getSellorMode(int userId) {
        return sellorModeRepo.findByUserIdCustom(userId);
    }

//    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



}