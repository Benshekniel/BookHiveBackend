package service.BookStore;

import model.dto.BookStore.BSStatDTOs;
import model.entity.BSBook;
import model.entity.BSBook.ListingType;
import model.entity.BSBook.BookStatus;

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
public class BSStatsService {

    private final BookStoreRepo bookStoreRepo;
    private final BSBookRepo bookRepo;

//    private final static List<ListingType> lendListingTypes = List.of(ListingType.LEND_ONLY, ListingType.SELL_AND_LEND);
//    private final static List<ListingType> sellListingTypes = List.of(ListingType.SELL_ONLY, ListingType.SELL_AND_LEND);
//
//    private final static List<BookStatus> lendBookStatuses = List.of(BookStatus.AVAILABLE, BookStatus.LENT);

    public int getBookStoreStats (Integer storeId) {
        return 7;
    }

    public BSStatDTOs.SaleStatDTO getSaleListingsStats (Integer storeId) {
        return bookRepo.getSaleStats(storeId);
    }
    public BSStatDTOs.LendStatDTO getLendListingsStats (Integer storeId) {
        return bookRepo.getLendStats(storeId);
    }

    public BSStatDTOs.RegularInventoryStatDTO getRegularInventoryStats (Integer storeId) {
        return bookRepo.getRegularInventoryStats(storeId);
    }

    public BSStatDTOs.DonationInventoryStatDTO getDonationInventoryStats (Integer storeId) {
        return bookRepo.getDonationInventoryStats(storeId);
    }
}


//List<ListingType> listingTypes = List.of(ListingType.LEND_ONLY, ListingType.SELL_AND_LEND);
//BSStatDTOs.SaleStatDTO statDTO = new BSStatDTOs.SaleStatDTO();
//
//        statDTO.setTotalBooks(bookRepo.countByBookStore_StoreIdAndListingTypeIn(storeId, listingTypes));
//        statDTO.setActiveListings(bookRepo.countByBookStore_StoreIdAndStatusAndListingTypeIn(storeId, BookStatus.AVAILABLE, listingTypes));


//        statDTO.setTotalBooks(bookRepo.countByBookStore_StoreIdAndListingTypeIn(storeId, lendListingTypes));
//        statDTO.setActiveListings(bookRepo.countByBookStore_StoreIdAndStatusNotAndListingTypeIn(storeId, BookStatus.INVENTORY, lendListingTypes));
//        statDTO.setInInventory(bookRepo.countByBookStore_StoreIdAndStatus(storeId, BookStatus.INVENTORY));
//        statDTO.setOnLoanCount(bookRepo.countByBookStore_StoreId);
//        statDTO.setAvgLendPrice(bookRepo.);
