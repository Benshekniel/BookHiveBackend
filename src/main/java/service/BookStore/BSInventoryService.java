//package service.BookStore;
//
//import model.dto.BookStore.BSInventoryDTOs;
//import model.dto.BookStore.BSStatDTOs;
//
//import java.util.List;
//
//public interface BSInventoryService {
//
//    boolean createInventory(BSInventoryDTOs.RegisterDTO registerDTO, Integer storeId);
//    boolean editInventoryItem(BSInventoryDTOs.EditDTO editDTO);
//
//    List<BSInventoryDTOs.ConciseRegularDTO>  getRegularInventory(Integer storeId);
//    List<BSInventoryDTOs.ConciseDonationDTO> getDonationInventory(Integer storeId);
//
//    BSInventoryDTOs.FullInventoryDTO getInventoryItem(Integer inventoryId);
//
//    BSInventoryDTOs.UpdateCountDTO getStockUpdateItem(Integer inventoryId);
//    boolean                        adjustStockChange(Integer inventoryId, Integer stockChange);
//    boolean                        adjustStock (BSInventoryDTOs.UpdateCountDTO updateCountDTO);
//
//    BSStatDTOs.RegularInventoryDTO getRegularInventoryStats (Integer storeId);
//    BSStatDTOs.DonationInventoryDTO getDonationInventoryStats (Integer storeId);
//
//    boolean unmarkForDonation (Integer inventoryId);
//    boolean deleteItem(Integer inventoryId);
//
//}
