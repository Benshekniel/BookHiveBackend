package service.BookStore;

import lombok.RequiredArgsConstructor;
import model.dto.BookStore.BookStoreDTO.*;
import model.entity.BookStore;
import model.entity.AllUsers;
import model.repo.BookStore.BookStoreRepo;
import model.repo.AllUsersRepo;

import org.springframework.stereotype.Service;

//import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookStoreServiceImpl implements BookStoreService {

    private final BookStoreRepo bookStoreRepo;
    private final AllUsersRepo allUsersRepo;

    // Create bookstore
    @Override
    public BookStoreResponseDTO createBookStore(BookStoreCreateDTO createDTO, Long userId) {
        // Get the actual user object
        AllUsers user = allUsersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user has BOOKSTORE role
        if (!"BOOKSTORE".equals(user.getRole())) {
            throw new RuntimeException("User must have BOOKSTORE role");
        }

        // Check if user already has a bookstore
//        if (bookStoreRepo.existsByUser(user)) {
//            throw new RuntimeException("User already has a bookstore");
//        }

        BookStore bookStore = convertCreateDTOToEntity(createDTO);

        BookStore savedBookStore = bookStoreRepo.save(bookStore);
        return convertEntityToResponseDTO(savedBookStore);
    }

    // Get all bookstores
//    @Override
//    public List<BookStoreListDTO> getAllBookStores() {
//        List<BookStore> bookStores = bookStoreRepo.findAll();
//        return bookStores.stream()
//                .map(this::convertEntityToListDTO)
//                .collect(Collectors.toList());
//    }

    // Get bookstore by store ID (public view)
//    @Override
//    public Optional<BookStorePublicDTO> getBookStoreById(Integer storeId) {
//        Optional<BookStore> bookStore = bookStoreRepo.findById(storeId);
//        return bookStore.map(this::convertEntityToPublicDTO);
//    }

    // Get bookstore by user ID (for owner dashboard)
//    @Override
//    public Optional<BookStoreResponseDTO> getBookStoreByUserId(Long userId) {
//        Optional<BookStore> bookStore = bookStoreRepo.findByUserId(userId);
//        return bookStore.map(this::convertEntityToResponseDTO);
//    }

    // Update bookstore
    @Override
    public BookStoreResponseDTO updateBookStore(BookStoreUpdateDTO updateDTO, Long userId) {
        Optional<BookStore> existingBookStoreOpt = bookStoreRepo.findById(updateDTO.getStoreId());

        if (existingBookStoreOpt.isEmpty()) {
            throw new RuntimeException("BookStore not found");
        }

        BookStore existingBookStore = existingBookStoreOpt.get();

        // Check if user owns this bookstore
        if (!existingBookStore.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this bookstore");
        }

        // Update fields
        updateEntityFromUpdateDTO(existingBookStore, updateDTO);

        BookStore updatedBookStore = bookStoreRepo.save(existingBookStore);
        return convertEntityToResponseDTO(updatedBookStore);
    }

    // Delete bookstore
    @Override
    public void deleteBookStore(Integer storeId, Long userId) {
        Optional<BookStore> bookStoreOpt = bookStoreRepo.findById(storeId);

        if (bookStoreOpt.isEmpty()) {
            throw new RuntimeException("BookStore not found");
        }

        BookStore bookStore = bookStoreOpt.get();

        // Check if user owns this bookstore
        if (!bookStore.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this bookstore");
        }

        bookStoreRepo.deleteById(storeId);
    }

    // Search bookstores
//    @Override
//    public List<BookStoreListDTO> searchBookStores(String query) {
//        List<BookStore> bookStores = bookStoreRepo.findByStoreNameContainingIgnoreCase(query);
//        return bookStores.stream()
//                .map(this::convertEntityToListDTO)
//                .collect(Collectors.toList());
//    }

    // Get bookstores by type
//    @Override
//    public List<BookStoreListDTO> getBookStoresByType(String booksType) {
//        BookStore.BookType type = BookStore.BookType.valueOf(booksType.toUpperCase());
//        List<BookStore> bookStores = bookStoreRepo.findByBooksType(type);
//        return bookStores.stream()
//                .map(this::convertEntityToListDTO)
//                .collect(Collectors.toList());
//    }

    // Conversion methods
    private BookStore convertCreateDTOToEntity(BookStoreCreateDTO dto) {
        BookStore bookStore = new BookStore();
        bookStore.setStoreName(dto.getStoreName());
        bookStore.setStoreImageURL(dto.getStoreImageURL());
        bookStore.setDescription(dto.getDescription());
        bookStore.setBusinessHours(dto.getBusinessHours());
        bookStore.setBooksType(BookStore.BookType.valueOf(dto.getBooksType().toUpperCase()));
        return bookStore;
    }

    private BookStoreResponseDTO convertEntityToResponseDTO(BookStore bookStore) {
        return new BookStoreResponseDTO(
                bookStore.getStoreId(),
                bookStore.getStoreName(),
                bookStore.getStoreImageURL(),
                bookStore.getDescription(),
                bookStore.getBusinessHours(),
                bookStore.getBooksType().toString(),
                bookStore.getUserId(),
                bookStore.getStoreName(),
                bookStore.getEmail(),
                bookStore.getPhoneNumber(),
                bookStore.getAddress(),
                bookStore.getCreatedAt() // registeredDate from user
        );
    }

    private BookStoreListDTO convertEntityToListDTO(BookStore bookStore) {
        return new BookStoreListDTO(
                bookStore.getStoreId(),
                bookStore.getStoreName(),
                bookStore.getStoreImageURL(),
                bookStore.getDescription(),
                bookStore.getBooksType().toString(),
                bookStore.getStoreName(),
                bookStore.getAddress(),
                bookStore.getCreatedAt() // registeredDate from user
        );
    }

    private BookStorePublicDTO convertEntityToPublicDTO(BookStore bookStore) {
        return new BookStorePublicDTO(
                bookStore.getStoreId(),
                bookStore.getStoreName(),
                bookStore.getStoreImageURL(),
                bookStore.getDescription(),
                bookStore.getBusinessHours(),
                bookStore.getBooksType().toString(),
                bookStore.getStoreName(),
                bookStore.getPhoneNumber(),
                bookStore.getAddress(),
                bookStore.getCreatedAt() // registeredDate from user
        );
    }

    private void updateEntityFromUpdateDTO(BookStore bookStore, BookStoreUpdateDTO dto) {
        if (dto.getStoreName() != null) bookStore.setStoreName(dto.getStoreName());
        if (dto.getStoreImageURL() != null) bookStore.setStoreImageURL(dto.getStoreImageURL());
        if (dto.getDescription() != null) bookStore.setDescription(dto.getDescription());
        if (dto.getBusinessHours() != null) bookStore.setBusinessHours(dto.getBusinessHours());
        if (dto.getBooksType() != null) bookStore.setBooksType(BookStore.BookType.valueOf(dto.getBooksType().toUpperCase()));
    }
}