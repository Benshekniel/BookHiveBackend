//package service.BookStore;
//
//import model.entity.BSTransaction;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//public interface BSTransactionService {
//    BSTransaction recordSale(Integer inventoryId, Integer userId, int quantity, BigDecimal price);
//    BSTransaction recordLend(Integer bookId, Integer userId, BigDecimal price);
//    BSTransaction recordReturn(Integer bookId, Integer userId);
//    BSTransaction recordDonation(Integer inventoryId, Integer userId, int quantity);
//    List<BSTransaction> getTransactionsByStore(Integer storeId);
//    List<BSTransaction> getTransactionsByUser(Integer userId);
//}
//
