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
public class BSListingsService {

    private final BookStoreRepo bookStoreRepo;
    private final BSBookRepo bookRepo;

    /** Common mapper resource for the entire service class: */
    private static final ModelMapper modelMapper = new ModelMapper();

    // get lists of books - 1.for sale, 2.for lending
    // sales overview - total, active listings
    // lending overview - total, on loan, avg price, avg duration

    public List<BSBookDTOs.BookListingDTO> getBookListingSale (Integer storeId) {
        List<BSBook> bookList = bookRepo.findByBookStore_StoreIdAndListingTypeIn(storeId,
                List.of(BSBook.ListingType.SELL_ONLY, BSBook.ListingType.SELL_AND_LEND));

        if (bookList.isEmpty())
            return Collections.emptyList();
        return bookList.stream()
                .map(book -> modelMapper.map(book, BSBookDTOs.BookListingDTO.class))
                .toList();
    }

    public List<BSBookDTOs.BookListingDTO> getBookListingLend (Integer storeId) {
        List<BSBook> bookList = bookRepo.findByBookStore_StoreIdAndListingTypeIn(storeId,
                List.of(BSBook.ListingType.LEND_ONLY, BSBook.ListingType.SELL_AND_LEND));

        if (bookList.isEmpty())
            return Collections.emptyList();
        return bookList.stream()
                .map(book -> modelMapper.map(book, BSBookDTOs.BookListingDTO.class))
                .toList();
    }

    public BSStatDTOs.SaleStatDTO getSaleStats (Integer storeId) {
        List<BSBook.ListingType> listingTypes = List.of(BSBook.ListingType.LEND_ONLY, BSBook.ListingType.SELL_AND_LEND);
        BSStatDTOs.SaleStatDTO statDTO = new BSStatDTOs.SaleStatDTO();

        statDTO.setTotalBooks(bookRepo.countByBookStore_StoreIdAndListingTypeIn(storeId, listingTypes));
        statDTO.setActiveListings(bookRepo.countByBookStore_StoreIdAndStatusAndListingTypeIn(storeId, BSBook.BookStatus.AVAILABLE, listingTypes));
        return statDTO;
    }

}
