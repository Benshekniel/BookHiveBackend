package service.BookStore;

import model.dto.BookStore.BSStatDTOs;
import model.dto.BookStore.BSBookDTOs;
import model.entity.BSBook;
import model.repo.BSBookRepo;
import model.repo.BookStoreRepo;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BSInventoryService {

    private final BookStoreRepo bookStoreRepo;
    private final BSBookRepo bookRepo;

    /** Common mapper resource for the entire service class: */
    private static final ModelMapper modelMapper = new ModelMapper();

    // get lists of books - all with bookStatus == inventory, listingTypeNotIn donate, others default
    // sales overview - total, active listings
    // lending overview - total, on loan, avg price, avg duration

    public List<BSBookDTOs.BookListingDTO> getRegularInventory (Integer storeId) {
        List<BSBook> bookList = bookRepo.findByBookStore_StoreIdAndStatusAndListingTypeNot (
                storeId, BSBook.BookStatus.INVENTORY, BSBook.ListingType.DONATE);

        if (bookList.isEmpty())
            return Collections.emptyList();
        return bookList.stream()
                .map(book -> modelMapper.map(book, BSBookDTOs.BookListingDTO.class))
                .toList();
    }

    public List<BSBookDTOs.BookListingDTO> getDonationInventory (Integer storeId) {
        List<BSBook> bookList = bookRepo.findByBookStore_StoreIdAndStatusAndListingType (
                storeId, BSBook.BookStatus.INVENTORY, BSBook.ListingType.DONATE);

        if (bookList.isEmpty())
            return Collections.emptyList();
        return bookList.stream()
                .map(book -> modelMapper.map(book, BSBookDTOs.BookListingDTO.class))
                .toList();
    }
}
