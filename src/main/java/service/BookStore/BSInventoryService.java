package service.BookStore;

import model.dto.BookStore.BSInventoryDTOs;

import java.util.List;

public interface BSInventoryService {
    boolean                                 createInventory(BSInventoryDTOs.RegisterDTO registerDTO, Integer storeId);
    boolean                                 editInventoryItem(BSInventoryDTOs.EditDTO editDTO);

    List<BSInventoryDTOs.ConciseRegularDTO> getRegularInventory(Integer storeId);
    List<BSInventoryDTOs.ConciseDonationDTO> getDonationInventory(Integer storeId);

    BSInventoryDTOs.FullInventoryDTO        getInventoryItem(Integer inventoryId);

    BSInventoryDTOs.UpdateCountDTO          getStockUpdateItem(Integer inventoryId);
    boolean                                 adjustStockChange(Integer inventoryId, Integer stockChange);
    boolean                                 adjustStock (BSInventoryDTOs.UpdateCountDTO updateCountDTO);
    boolean                                 deleteItem(Integer inventoryId);

}
