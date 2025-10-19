package controller.BookStore;

import model.dto.BookStore.BSStatDTOs;
import model.entity.AllUsers;
import model.entity.BookStore;
import model.repo.BookStore.BookStoreRepo;
//import service.BookStore.BSInventoryService;
//import service.BookStore.BookStoreService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import service.BookStore.BSInventoryService;

@RestController
@RequestMapping("/api/bs-test")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class BSTestController {

    private final BSInventoryService inventoryService;
//    private final BookStoreService bookStoreService;
    private final BookStoreRepo bookStoreRepo;

    @GetMapping("/repoTest/{userId}")
    public ResponseEntity<BSStatDTOs.RegularInventoryDTO> test(
            @PathVariable("userId") Integer userId ) {
        AllUsers allUsers = new AllUsers();
        allUsers.setUser_id(userId);

        BookStore store =  bookStoreRepo.findByAllUserNew(userId);
        Integer storeId = store.getStoreId();

        return ResponseEntity.ok(inventoryService.getRegularInventoryStats(storeId));

//        return ResponseEntity.ok(storeId.toString());
    }

}
