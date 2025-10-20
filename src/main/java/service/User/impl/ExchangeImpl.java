package service.User.impl;

import model.dto.Bidding.Exchange_BooksDTO;
import model.entity.Bid.Exchange_Books;
import model.entity.UserBooks;
import model.repo.bid.ExchangeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.User.ExchangeService;

import java.util.List;

@Service
public class ExchangeImpl implements ExchangeService {

    @Autowired
    private ExchangeRepo exchangeRepo;

    @Override
    public String createExchange(Exchange_BooksDTO exchangeDTO) {

        // 🔍 Step 1: Find approverId using email
        Integer approverId = exchangeRepo.findApproverIdByApproverEmail(
                String.valueOf(exchangeDTO.getApproverEmail()) // ensure your DTO type is String ideally
        );

        if (approverId == null) {
            return "approver_not_found";
        }

        // 🧱 Step 2: Build Exchange_Books entity
        Exchange_Books exchange = new Exchange_Books(
                exchangeDTO.getApproverBookId(),
                exchangeDTO.getUserBookId(),
                exchangeDTO.getUserId(),
                approverId,
                exchangeDTO.getUserDeliveryPrice(),
                exchangeDTO.getUserHandlingPrice(),
                "PENDING" // auto set status
        );

        // 🧾 Step 3: Save to DB
        exchangeRepo.save(exchange);

        return "success";
    }


    @Override
    public Exchange_Books createExchangeAndReturnEntity(Exchange_BooksDTO exchangeDTO) {
        Integer approverId = exchangeRepo.findApproverIdByApproverEmail(exchangeDTO.getApproverEmail());
        if (approverId == null) return null;

        Exchange_Books exchange = new Exchange_Books(
                exchangeDTO.getApproverBookId(),
                exchangeDTO.getUserBookId(),
                exchangeDTO.getUserId(),
                approverId,
                exchangeDTO.getUserDeliveryPrice(),
                exchangeDTO.getUserHandlingPrice(),
                "PENDING"
        );

        return exchangeRepo.save(exchange);
    }

    @Override
    public List<UserBooks> getAllBooksByEmail(String email) {
        return exchangeRepo.findAllBooksByUserEmail(email);
    }

    @Override
    public boolean checkExchangeExists(int userId, Long bookId) {
        return exchangeRepo.existsByUserIdAndBookId(userId, bookId);
    }

    /// //////////////////////////////////////////////////////////////

    @Override
    public List<Exchange_Books> getOutgoingExchanges(int userId) {
        return exchangeRepo.findAllOutgoingExchangesByUserId(userId);
    }

    @Override
    public List<Exchange_Books> getIncomingExchanges(int userId) {
        return exchangeRepo.findAllIncomingExchangesByApproverId(userId);
    }

    @Override
    public UserBooks getBookById(Long bookId) {
        return exchangeRepo.findBookByBookId(bookId);
    }

    @Override
    public String approveExchange(Integer exchangeId, double deliveryFee, double handlingFee) {
        int updated = exchangeRepo.approveExchange(exchangeId, deliveryFee, handlingFee);
        return updated > 0 ? "approved" : "not_found";
    }

    @Override
    public String rejectExchange(Integer exchangeId, String reason) {
        int updated = exchangeRepo.rejectExchange(exchangeId, reason);
        return updated > 0 ? "rejected" : "not_found";
    }

    // ✅ Full logic: get address by user_id (via email lookup)
    public String getAddressByUserId(int userId) {
        String email = exchangeRepo.findEmailByUserId(userId);
        if (email == null) return null;
        return exchangeRepo.findAddressByEmail(email);
    }

    // ✅ Get address using email
    @Override
    public String getAddressByEmail(String email) {
        return exchangeRepo.findAddressByEmail(email);
    }
}
