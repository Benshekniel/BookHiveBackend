package service.BookStore.Impl;

import model.dto.BookStore.BSStatDTOs;
import model.entity.BSInventory;
import model.dto.BookStore.BSInventoryDTOs;
import model.entity.BookStore;
import model.repo.BookStore.BSInventoryRepo;
import model.repo.BookStore.BookStoreRepo;
import service.BookStore.BSInventoryService;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

@Service @Transactional
@RequiredArgsConstructor
public class BSInventoryServiceImpl implements BSInventoryService {
    
    private final BSInventoryRepo inventoryRepo;
    private final BookStoreRepo storeRepo;

    private static final ModelMapper modelMapper = new ModelMapper();
    
    public boolean createInventory (BSInventoryDTOs.RegisterDTO registerDTO, Integer storeId) {
        BSInventory inventory = modelMapper.map(registerDTO, BSInventory.class);
            BookStore bookStore = new BookStore();
            bookStore.setStoreId(storeId);
            inventory.setBookStore(bookStore);
        inventoryRepo.save(inventory);
        return true;
    }

    public boolean editInventoryItem (BSInventoryDTOs.EditDTO editDTO) {
        Integer inventoryId = editDTO.getInventoryId();
        Optional<BSInventory> bookOpt = inventoryRepo.findByInventoryId (inventoryId);
        if (bookOpt.isEmpty())
            return false;
        else {
            BSInventory item = bookOpt.get();

            if (editDTO.getCoverImage() == null) editDTO.setCoverImage(item.getCoverImage());
            modelMapper.map(editDTO, item);
            inventoryRepo.save(item);
            return true;
        }
    }

    public List<BSInventoryDTOs.ConciseRegularDTO> getRegularInventory(Integer storeId) {
        List<BSInventory> bookList = inventoryRepo.findAllByBookStore_StoreIdAndIsForDonationFalse(storeId);
        if (bookList.isEmpty())
            return Collections.emptyList();
        return bookList.stream()
                .map(item -> modelMapper.map(item, BSInventoryDTOs.ConciseRegularDTO.class))
                .toList();
    }
    public List<BSInventoryDTOs.ConciseDonationDTO> getDonationInventory(Integer storeId) {
        List<BSInventory> bookList = inventoryRepo.findAllByBookStore_StoreIdAndIsForDonationTrue(storeId);
        if (bookList.isEmpty())
            return Collections.emptyList();
        return bookList.stream()
                .map(item -> modelMapper.map(item, BSInventoryDTOs.ConciseDonationDTO.class))
                .toList();
    }

    public BSInventoryDTOs.FullInventoryDTO getInventoryItem(Integer inventoryId) {
        Optional<BSInventory> bookOpt = inventoryRepo.findByInventoryId (inventoryId);
        if (bookOpt.isEmpty())
            return null;
        else {
            BSInventory item = bookOpt.get();
            return modelMapper.map(item, BSInventoryDTOs.FullInventoryDTO.class);
        }
    }

    public BSInventoryDTOs.UpdateCountDTO getStockUpdateItem(Integer inventoryId) {
        Optional<BSInventory> bookOpt = inventoryRepo.findByInventoryId (inventoryId);
        if (bookOpt.isEmpty())
            return null;
        else {
            BSInventory item = bookOpt.get();
            return modelMapper.map(item, BSInventoryDTOs.UpdateCountDTO.class);
        }
    }

    public boolean adjustStockChange (Integer inventoryId, Integer stockChange) {
        Optional<BSInventory> bookOpt = inventoryRepo.findByInventoryId (inventoryId);
        if (bookOpt.isEmpty())
            return false;
        else {
            BSInventory item = bookOpt.get();
            item.setStockCount(item.getStockCount() + stockChange);
            inventoryRepo.save(item);
            return true;
        }
    }

    public boolean adjustStock (BSInventoryDTOs.UpdateCountDTO updateCountDTO) {
        Optional<BSInventory> bookOpt = inventoryRepo.findByInventoryId (updateCountDTO.getInventoryId());
        if (bookOpt.isEmpty())
            return false;
        else {
            BSInventory item = bookOpt.get();
            item.setStockCount(updateCountDTO.getStockCount());
            item.setSellableCount(updateCountDTO.getSellableCount());
            inventoryRepo.save(item);
            return true;
        }
    }

    public BSStatDTOs.RegularInventoryDTO getRegularInventoryStats(Integer storeId) {
        BSStatDTOs.RegularInventoryDTO statDTO = new BSStatDTOs.RegularInventoryDTO();

        statDTO.setTotalBooks(inventoryRepo.getTotalRegularStockByStoreId(storeId));
        statDTO.setTotalSellable(inventoryRepo.getSellableRegularStockByStoreId(storeId));
        statDTO.setLowStockAlerts(inventoryRepo.countByBookStore_StoreIdAndIsForDonationFalseAndStockCountLessThan(storeId, 5));
        statDTO.setNewBooks(inventoryRepo.getTotalNewRegularStockByStoreId(storeId));

        statDTO.setLowStockTitles(inventoryRepo.findTop3ByBookStore_StoreIdAndIsForDonationFalseOrderByStockCountAsc(storeId)
                .stream().map(BSInventory::getTitle).toList());
        statDTO.setTopTitles(inventoryRepo.findTop3ByBookStore_StoreIdAndIsForDonationFalseOrderByStockCountDesc(storeId)
                .stream().map(BSInventory::getTitle).toList());

        return statDTO;
    }

    public BSStatDTOs.DonationInventoryDTO getDonationInventoryStats(Integer storeId) {
        BSStatDTOs.DonationInventoryDTO statDTO = new BSStatDTOs.DonationInventoryDTO();

        statDTO.setTotalDonationInventory(inventoryRepo.getTotalDonationStockByStoreId(storeId));
        statDTO.setDonatedCount(storeRepo.getDonatedCountByStoreId(storeId));
        statDTO.setDonatedClientCount(storeRepo.getDonatedClientCountByStoreId(storeId));

        return statDTO;
    }

    public boolean unmarkForDonation(Integer inventoryId) {
        return inventoryRepo.findByInventoryId(inventoryId)
                .map(existingItem -> {
                    existingItem.setIsForDonation(false);
                    inventoryRepo.save(existingItem);

                    return true;
                })
                .orElse(false);
    }

    public boolean deleteItem(Integer inventoryId) {
        return inventoryRepo.findByInventoryId(inventoryId)
                .map(existingItem -> {
                    existingItem.setIsDeleted(true);
                    inventoryRepo.save(existingItem);

                    return true;
                })
                .orElse(false);
    }

//    public boolean updateInventoryCount(Integer inventoryId, int delta) {
//
//    }
//    public List<BSInventory> getInventoryByStore(Integer storeId) {
//
//    }
//    public boolean softDeleteInventory(Integer inventoryId) {
//
//    }
}
