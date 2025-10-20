//package controller.BookStore;
//
//import model.dto.BookStore.BSForUserDTOs.FullInventoryDTO;
//import model.dto.BookStore.BSForUserDTOs.FullBookDTO;
//import service.BookStore.BSForUsersService;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/bs-user")
//@CrossOrigin(origins = "http://localhost:9999")
//@RequiredArgsConstructor
//public class BSForUsersController {
//
//    private final BSForUsersService bsforusersService;
//
//    @GetMapping("/allBooks")
//    public ResponseEntity<List<FullBookDTO>> getAllIndividualBooks () {
//        List<FullBookDTO> books = bsforusersService.getBSBooks();
//
//        if (books.isEmpty())
//            return ResponseEntity.noContent().build();
//        return ResponseEntity.ok(books);
//    }
//
//    @GetMapping("/allInventory")
//    public ResponseEntity<List<FullInventoryDTO>> getAllInventory () {
//        List<FullInventoryDTO> books = bsforusersService.getBSInventory();
//
//        if (books.isEmpty())
//            return ResponseEntity.noContent().build();
//        return ResponseEntity.ok(books);
//    }
//}
