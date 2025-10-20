package service.User;

import model.dto.Bidding.Exchange_BooksDTO;
import model.entity.Bid.Exchange_Books;
import model.entity.UserBooks;

import java.util.List;

public interface ExchangeService {

    String createExchange(Exchange_BooksDTO exchangeDTO);

    public Exchange_Books createExchangeAndReturnEntity(Exchange_BooksDTO exchangeDTO);

    List<UserBooks> getAllBooksByEmail(String email);

    boolean checkExchangeExists(int userId, Long bookId);

    // Outgoing requests
    List<Exchange_Books> getOutgoingExchanges(int userId);

    // Incoming requests
    List<Exchange_Books> getIncomingExchanges(int userId);

    // Single book by bookId
    UserBooks getBookById(Long bookId);

    // Approve exchange
    String approveExchange(Integer exchangeId, double deliveryFee, double handlingFee);

    // Reject exchange
    String rejectExchange(Integer exchangeId, String reason);

    public String getAddressByUserId(int userId);

    public String getAddressByEmail(String email);

}
