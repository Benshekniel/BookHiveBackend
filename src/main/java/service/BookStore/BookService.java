package service.BookStore;

import model.repo.BookRepo;
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
public class BookService {

    private final BookRepo bookRepo;

    // Common mapper resource for the entire service class:
    private static final ModelMapper modelMapper = new ModelMapper();


}
