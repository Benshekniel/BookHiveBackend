package service.BookStore;

import model.dto.BookStoreDTOs.RegisterBookStoreDTO;
import model.dto.BookStoreDTOs.ProfileBookStoreDTO;
import model.entity.BookStore;
import model.repo.BookStoreRepo;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@Service
@RequiredArgsConstructor
@Transactional
public class BookStoreService {

    private final BookStoreRepo bookStoreRepo;

    // Common mapper resource for the entire service class:
    private static final ModelMapper modelMapper = new ModelMapper();

    public ResponseEntity<String> registerBookStore(RegisterBookStoreDTO bookStoreDTO) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        BookStore bookStore = modelMapper.map(bookStoreDTO, BookStore.class);
        bookStoreRepo.save(bookStore);
        return ResponseEntity.ok("Book store registered successfully");
    }

    public ResponseEntity<ProfileBookStoreDTO> getBookStoreById(Integer id) {
        return bookStoreRepo.findByStoreId(id)
                .map(existingBookStore -> {
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                    ProfileBookStoreDTO bookStoreProfile = modelMapper.map(existingBookStore, ProfileBookStoreDTO.class);
                    return ResponseEntity.ok(bookStoreProfile);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<String> updateBookStore(Integer id, ProfileBookStoreDTO bookStoreDTO) {
        return bookStoreRepo.findByStoreId(id)
                .map(existingBookStore -> {
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                    modelMapper.map(bookStoreDTO, existingBookStore);

                    bookStoreRepo.save(existingBookStore);
                    return ResponseEntity.ok("BookStore updated successfully");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("BookStore Not found!"));
    }

}
