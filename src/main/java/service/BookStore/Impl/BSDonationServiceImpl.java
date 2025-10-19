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

    public List<BSInventoryDTOs.ConciseDonationDTO> getInventoryListByCategory (String category) {
        List<BSInventory> itemList = inventoryRepo.findAllByCategory(category);
        if (itemList.isEmpty())
            return Collections.emptyList();
        return itemList.stream()
                .map(item -> modelMapper.map(item, BSInventoryDTOs.ConciseDonationDTO.class))
                .toList();
    }

}
