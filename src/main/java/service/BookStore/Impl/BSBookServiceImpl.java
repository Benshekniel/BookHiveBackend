package service.BookStore.Impl;

import model.dto.BookStore.BSBookDTOs;
import model.dto.BookStore.BSInventoryDTOs;
import model.dto.BookStore.BSStatDTOs;
import model.entity.BSBook;
import model.entity.BSInventory;
import model.entity.BookStore;
import model.repo.BookStore.BSBookRepo;
import model.repo.BookStore.BSInventoryRepo;
import service.BookStore.BSBookService;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import service.BookStore.BSInventoryService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service @Transactional
@RequiredArgsConstructor
public class BSBookServiceImpl implements BSBookService {

    // Common mapper resource for the entire service class:
    private static final ModelMapper modelMapper = new ModelMapper();

    private final BSBookRepo bookRepo;
    private final BSInventoryRepo inventoryRepo;
    private final BSInventoryService inventoryService;

    public boolean createBookFromInventory ( BSInventoryDTOs.ToBSBookDTO toBSBookDTO ) {

        Integer inventoryID = toBSBookDTO.getInventoryId();
        Optional<BSInventory> inventoryOpt = inventoryRepo.findById(inventoryID);
        if (inventoryOpt.isEmpty())
            return false;
        if (!inventoryService.adjustStockChange(inventoryID, -1))
            return false;

        BSInventory inventory = inventoryOpt.get();

        BSBook book = new BSBook();
        modelMapper.map(inventory, book);
        modelMapper.map(toBSBookDTO, book);

        book.setBookId(null);
        bookRepo.save(book);
        return true;
    }

    @Override
    public boolean createBook(BSBookDTOs.RegisterDTO registerDTO, Integer storeId) {
        return false;
    }

//    public boolean createBook (BSBookDTOs.RegisterDTO registerDTO, Integer storeId) {
//        BSBook book = modelMapper.map(registerDTO, BSBook.class);
//            BookStore bookStore = new BookStore();
//            bookStore.setStoreId(storeId);
//            book.setBookStore(bookStore);
//        bookRepo.save(book);
//        return true;
//    }

    public List<BSBookDTOs.ConciseLendOnlyDTO> getLendOnlyList(Integer storeId) {
        List<BSBook> bookList = bookRepo.findByBookStore_StoreIdAndIsForSellingFalse(storeId);
        if (bookList.isEmpty())
            return Collections.emptyList();
        return bookList.stream()
                .map(item -> modelMapper.map(item, BSBookDTOs.ConciseLendOnlyDTO.class))
                .toList();
    }

    public List<BSBookDTOs.ConciseSellAlsoDTO> getSellAlsoList(Integer storeId) {
        List<BSBook> bookList = bookRepo.findByBookStore_StoreIdAndIsForSellingTrue(storeId);
        if (bookList.isEmpty())
            return Collections.emptyList();
        return bookList.stream()
                .map(item -> modelMapper.map(item, BSBookDTOs.ConciseSellAlsoDTO.class))
                .toList();
    }

    public BSBookDTOs.FullBookDTO getBookItem(Integer bookId) {
        Optional<BSBook> bookOpt = bookRepo.findByBookId (bookId);
        if (bookOpt.isEmpty())
            return null;
        else {
            BSBook book = bookOpt.get();
            return modelMapper.map(book, BSBookDTOs.FullBookDTO.class);
        }
    }


    public BSStatDTOs.LendOnlyStatDTO getLendOnlyStats(Integer storeId) {
        return null;
    }
    public BSStatDTOs.SellAlsoStatDTO getSellAlsoStats(Integer storeId) {
        return null;
    }

    @Override
    public boolean editBook(BSBookDTOs.EditDTO editDTO) {
        return false;
    }

    @Override
    public boolean unmarkForSelling(Integer bookId) {
        return false;
    }

    @Override
    public boolean deleteBook(Integer inventoryId) {
        return false;
    }


//    public boolean editBook(BSBookDTOs.EditDTO editDTO) {
//        Integer bookId = editDTO.getBookId();
//        Optional<BSBook> bookOpt = bookRepo.findByBookId (bookId);
//        if (bookOpt.isEmpty())
//            return false;
//        else {
//            BSBook book = bookOpt.get();
//
//            if (editDTO.getCoverImage() == null) editDTO.setCoverImage(book.getCoverImage());
//            modelMapper.map(editDTO, book);
//            bookRepo.save(book);
//            return true;
//        }
//    }

//    public boolean unmarkForSelling (Integer bookId) {
//        return bookRepo.findByBookId(bookId)
//                .map(existingItem -> {
//                    existingItem.setIsForSelling(false);
//                    bookRepo.save(existingItem);
//                    return true;
//                })
//                .orElse(false);
//    }
//
//    public boolean deleteBook(Integer bookId) {
//        return bookRepo.findByBookId(bookId)
//                .map(existingItem -> {
//                    existingItem.setIsDeleted(true);
//                    bookRepo.save(existingItem);
//                    return true;
//                })
//                .orElse(false);
//    }
}
