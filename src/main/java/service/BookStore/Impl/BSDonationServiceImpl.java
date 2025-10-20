package service.BookStore.Impl;

import model.dto.BookStore.BSDonationDTO.DonationDetailsDTO;
import model.dto.BookStore.BSInventoryDTOs;
import model.entity.BSInventory;
import model.entity.Donation;
import model.repo.BookStore.BSDonationRepo;
import model.repo.BookStore.BSInventoryRepo;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import service.BookStore.BSDonationService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service @Transactional
@RequiredArgsConstructor
public class BSDonationServiceImpl implements BSDonationService {
    
    private final BSInventoryRepo inventoryRepo;
    private final BSDonationRepo donationRepo;

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
        List<BSInventory> itemList = inventoryRepo.findAllByBookStore_StoreIdAndCategory(storeId, category);
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
            if (newQuantity > donation.getQuantity()) {
                donation.setStatus("RECEIVED");
            }
            donationRepo.save(donation);
            return true;
        }
    }

}
