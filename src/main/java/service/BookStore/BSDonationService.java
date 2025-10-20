package service.BookStore;

import model.dto.BookStore.BSDonationDTO;
import model.dto.BookStore.BSInventoryDTOs;

import java.util.List;

public interface BSDonationService {

    List<BSDonationDTO.DonationDetailsDTO> getCurrentDonationEvents ();

    List<BSInventoryDTOs.ConciseDonationDTO> getInventoryListByCategory (String category, Integer storeId);

    boolean contributeToDonation (Long donationId, Integer addition);

    boolean fullDonationProcess ( Long donationId, Integer storeId, List<BSInventoryDTOs.ContributionDTO> contributions);
}
