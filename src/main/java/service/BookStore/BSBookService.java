package service.BookStore;

import model.dto.BookStore.BSBookDTOs;
import model.dto.BookStore.BSInventoryDTOs;
import model.entity.BSBook;

import java.util.List;

public interface BSBookService {
    boolean createBookFromInventory(BSInventoryDTOs.ToBSBookDTO toBSBookDTO);
    boolean createBook (BSBookDTOs.RegisterDTO registerDTO, Integer storeId);

    List<BSBookDTOs.ConciseLendOnlyDTO> getLendOnlyList (Integer storeId);
    List<BSBookDTOs.ConciseSellAlsoDTO> getSellAlsoList (Integer storeId);

    BSBookDTOs.FullBookDTO        getBookItem (Integer bookId);

    boolean                                 editBook(BSBookDTOs.EditDTO editDTO);
    boolean                                 deleteBook(Integer inventoryId);


}

