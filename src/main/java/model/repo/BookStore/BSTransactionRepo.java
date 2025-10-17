//package model.repo.BookStore;
//
//import model.entity.BSTransaction;
//import org.springframework.stereotype.Repository;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//@Repository
//public interface BSTransactionRepo extends JpaRepository<BSTransaction, Long> {
//    List<BSTransaction> findByBookStore_StoreId(Integer storeId);
//    List<BSTransaction> findByUser_UserId(Integer userId);
//}
//
