package service.User;

//import model.dto.BooksDTO;
import model.dto.Bidding.UserBidDTO;
import model.dto.UserBooksDTO;
import model.entity.Bid.Bid_History;
import model.entity.Bid.SellorMode;
import model.entity.Bid.User_Bid;

import java.util.List;

public interface BooksService {
    String addBook(UserBooksDTO userBooksDTO);
    List<UserBooksDTO> getAllBooks();
    UserBooksDTO getBookById(Long id);
    String updateBook(Long id, UserBooksDTO userBooksDTO);
    String deleteBook(Long id);

    public String updateTransactionDeliveryAddress(int allUsersId);

    public String placeUserBid(UserBidDTO userBidDTO);
    public List<User_Bid> getBidsByBookId(Long bookId);
    public List<Bid_History> getBidHistoryByBookId(Long bookId);

    public boolean updateBidWinner(Long bidId, String winnerName);

    public SellorMode setSellorMode(SellorMode sellorMode);

    public SellorMode getSellorMode(int userId);


    /// //////////////////////////////////////////////////////////////////////


}