package service.BookStore.Impl;

import model.dto.BookStore.BSDonationDTO.DonationDetailsDTO;
import model.dto.BookStore.BSInventoryDTOs;
import model.entity.BSInventory;
import model.entity.Donation;
import model.repo.BookStore.BSDonationRepo;
import model.repo.BookStore.BSInventoryRepo;

import model.repo.BookStore.BookStoreRepo;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import service.BookStore.BSDonationService;
import service.BookStore.BSInventoryService;
import service.BookStore.BookStoreService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service @Transactional
@RequiredArgsConstructor
public class BSDonationServiceImpl implements BSDonationService {
    
    private final BSInventoryRepo inventoryRepo;
    private final BSInventoryService inventoryService;
    private final BSDonationRepo donationRepo;
    private final BookStoreService bookStoreService;

    private static final ModelMapper modelMapper = new ModelMapper();

    public List<DonationDetailsDTO> getCurrentDonationEvents () {
        List<Donation> eventList = donationRepo.findAllByStatusEqualsIgnoreCase("APPROVED");
        if (eventList.isEmpty())
            return Collections.emptyList();
        return eventList.stream()
                .map(item -> modelMapper.map(item, DonationDetailsDTO.class))
                .toList();
    }

    public List<BSInventoryDTOs.ConciseDonationDTO> getInventoryListByCategory (String category, Integer storeId) {
        List<BSInventory> itemList = inventoryRepo.findAllByBookStore_StoreIdAndCategoryAndIsForDonationTrue(storeId, category);
        if (itemList.isEmpty())
            return Collections.emptyList();
        return itemList.stream()
                .map(item -> modelMapper.map(item, BSInventoryDTOs.ConciseDonationDTO.class))
                .toList();
    }



    public boolean contributeToDonation (Long donationId, Integer addition) {
        Optional<Donation> donationOpt = donationRepo.findById(donationId);
        if (donationOpt.isEmpty())
            return false;
        else {
            Donation donation = donationOpt.get();

            Integer newQuantity = donation.getQuantityCurrent() + addition;
            donation.setQuantityCurrent(newQuantity);
            if (newQuantity >= donation.getQuantity()) {
                donation.setStatus("RECEIVED");
            }
            donationRepo.save(donation);
            return true;
        }
    }

    public boolean fullDonationProcess (
            Long donationId, Integer storeId, List<BSInventoryDTOs.ContributionDTO> contributions) {

        if (contributions == null || contributions.isEmpty()) return false;

        int totalAddition = 0;
        boolean allInventoryUpdated = true;

        for (BSInventoryDTOs.ContributionDTO updateCount : contributions) {
            Integer invId = updateCount.getInventoryId();
            Integer change = updateCount.getContributionCount();
            if (invId == null || change == null || change <= 0) {
                allInventoryUpdated = false;
                break;
            }

            // reduce stock by contributionCount (note negative delta)
            boolean done = inventoryService.adjustStockChange(invId, -change);
            if (!done) {
                allInventoryUpdated = false; break;
            }
            totalAddition += change;
        }
        if (!allInventoryUpdated) return false;

        boolean contributed = this.contributeToDonation(donationId, totalAddition);
        boolean bookstoreUpdate = bookStoreService.updateDonatedCount(storeId, totalAddition);

        return (bookstoreUpdate && contributed);
    }

}
