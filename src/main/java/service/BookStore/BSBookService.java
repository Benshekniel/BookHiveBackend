package service.BookStore;

import model.dto.BookStore.BSBookDTOs.RegisterBookDTO;
import model.dto.BookStore.BSBookDTOs.UpdateBookDTO;
import model.dto.BookStore.BSBookDTOs.ViewBookDTO;
import model.entity.BSBook;
import model.repo.BSBookRepo;

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
public class BSBookService {

    private final BSBookRepo bookRepo;

    // Common mapper resource for the entire service class:
    private static final ModelMapper modelMapper = new ModelMapper();

    public ResponseEntity<String> registerBook (RegisterBookDTO bookDTO, Integer storeId) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        BSBook book = modelMapper.map(bookDTO, BSBook.class);
             book.setStoreId(storeId);
        bookRepo.save(book);
        return ResponseEntity.ok("Book registered successfully");
    }

    public ResponseEntity<ViewBookDTO> getBookById (Integer bookId) {
        return bookRepo.findByBookId(bookId)
                .map(existingBook -> {
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                    ViewBookDTO bookView = modelMapper.map(existingBook, ViewBookDTO.class);
                    return ResponseEntity.ok(bookView);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<String> updateBook (Integer bookId, UpdateBookDTO bookDTO) {
        return bookRepo.findByBookId(bookId)
                .map(existingBook -> {
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                    modelMapper.map(bookDTO, existingBook);

                    bookRepo.save(existingBook);
                    return ResponseEntity.ok("Book updated successfully");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book Not found!"));
    }

}
