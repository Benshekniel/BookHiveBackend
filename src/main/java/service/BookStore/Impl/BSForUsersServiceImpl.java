package service.BookStore.Impl;

//import model.dto.BookStore.BSBookDTOs;
import model.dto.BookStore.BSForUserDTOs.FullInventoryDTO;
import model.dto.BookStore.BSForUserDTOs.FullBookDTO;
//import model.dto.UserBooksDTO;
import model.entity.BSBook;
import model.entity.BSInventory;
import model.repo.BookStore.BSBookRepo;
import model.repo.BookStore.BSInventoryRepo;
import org.modelmapper.ModelMapper;
import service.BookStore.BSForUsersService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service @Transactional
@RequiredArgsConstructor
public class BSForUsersServiceImpl implements BSForUsersService {


    private final BSInventoryRepo inventoryRepo;
    private final BSBookRepo bookRepo;
    private static final ModelMapper modelMapper = new ModelMapper();

    /** The bulk stuff in inventory with stockCount */
    public List<FullInventoryDTO> getBSInventory () {
        List<BSInventory> bookList = inventoryRepo.findByIsForDonationFalse ();
        if (bookList.isEmpty())
            return Collections.emptyList();
        return bookList.stream()
                .map(item -> modelMapper.map(item, FullInventoryDTO.class))
                .toList();
    }

    /** The individual books */
    public List<FullBookDTO> getBSBooks () {
        List<BSBook> bookList = bookRepo.findAll();
        if (bookList.isEmpty())
            return Collections.emptyList();
        return bookList.stream()
                .map(item -> modelMapper.map(item, FullBookDTO.class))
                .toList();
    }


//    private UserBooksDTO convertInventoryToUserBook (BSInventory inventory) {
//        UserBooksDTO userBook = modelMapper.map(inventory, UserBooksDTO.class);
//        userBook.setForBidding(false);
//        userBook.setForExchange(false);
//        userBook.setForSale(true);
//        userBook.setForLend(false);
//
//        return userBook;
//    }
//    private UserBooksDTO convertBookToUserBook (BSBook book) {
//        UserBooksDTO userBook = modelMapper.map(book, UserBooksDTO.class);
//        userBook.setForBidding(false);
//        userBook.setForExchange(false);
//        userBook.setForSale(book.getIsForSelling());
//        userBook.setForLend(true);
//
//        return userBook;
//    }

}
