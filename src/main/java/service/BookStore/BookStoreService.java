package service.BookStore;

import model.dto.BookStoreDTO;
import model.repo.BookStoreRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookStoreService {

    private final BookStoreRepo bookStoreRepo;

//    public ResponseEntity<String> registerBookStore(BookStoreDTO.RegisterBookStoreDTO registerDTO) {
//        return ResponseEntity.ok("Hello World");
//    }
    public ResponseEntity<String> registerBookStore() {
        return ResponseEntity.ok("Hello World");
    }
}
