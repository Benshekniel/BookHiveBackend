package service.BookStore;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;

import model.repo.BSBookRepo;
import model.repo.BookStoreRepo;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class BSDashboardService {

    private final BookStoreRepo bookStoreRepo;
    private final BSBookRepo bookRepo;

    /** Common mapper resource for the entire service class: */
    private static final ModelMapper modelMapper = new ModelMapper();

//    getPerformance
//    getRecentOrders
//    getInventorySummary

    public void getPerformance (Integer storeId) {

    }
}
