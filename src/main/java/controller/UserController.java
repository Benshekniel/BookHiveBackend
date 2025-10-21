package controller;

import model.dto.*;
//import model.dto.BooksDTO;
import model.dto.Bidding.Exchange_BooksDTO;
import model.dto.Bidding.UserBidDTO;
import model.dto.Bidding.UserBorrowRequestDTO;
import model.entity.*;
import model.entity.Bid.*;
import model.messageResponse.LoginResponse;
import model.repo.Delivery.DeliveryRepository;
import model.repo.Delivery.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.Delivery.impl.TransactionService;
import service.FileUpload.UploadService;
import service.GoogleDriveUpload.FileStorageService;
import service.Login.LoginService;
import service.Moderator.CompetitionService;
import service.Moderator.TrustScoreRegulationService;
import service.User.BooksService;
import service.User.BorrowService;
import service.User.ExchangeService;
import service.User.UserCompetitionService;
import model.repo.UsersRepo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
@RequestMapping("/api")
public class UserController {

    @Autowired
    private BooksService booksService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserCompetitionService userCompetitionService;
    @Autowired
    private CompetitionService competitionService;

    @Autowired
    private TrustScoreRegulationService regulationService;

    @Autowired
    private TransactionRepository transactionService;

    @Autowired
    private BorrowService borrowRequestService;

    @Autowired
    private DeliveryRepository deliveryRepository;

    //Books APIs
    @PostMapping("/saveBook-User")
    public ResponseEntity<?> saveBookasUser(
            @RequestPart("coverImage") MultipartFile bookImage,
            @RequestPart("bookData") UserBooksDTO userBooksDTO)
            throws IOException
    {

        String bookImageName = fileStorageService.generateRandomFilename(bookImage);
        // Assign random filenames to DTO
        userBooksDTO.setBookImage(bookImageName);

        String response= booksService.addBook(userBooksDTO);
        if ("success".equals(response)) {
            Map<String, String> uploadResult = fileStorageService.uploadFile(bookImage, "userBooks", bookImageName);
            return ResponseEntity.ok(Map.of("message", response));
        }
        return ResponseEntity.ok(Map.of("message", response));
    }

    @GetMapping("/getBooks")
    public ResponseEntity<List<UserBooksDTO>> getAllBooks() {
        List<UserBooksDTO> books = booksService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/getBook/{id}")
    public ResponseEntity<UserBooksDTO> getBookById(@PathVariable Long id) {
        UserBooksDTO book = booksService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
            ("/updateBook/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody UserBooksDTO userBooksDTO) {
        String response = booksService.updateBook(id, userBooksDTO);
        return ResponseEntity.ok(Map.of("message", response));
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        String response = booksService.deleteBook(id);
        return ResponseEntity.ok(Map.of("message", response));
    }

    //Competition APIs

    @GetMapping("/getAllCompetitions")
    public ResponseEntity<List<Map<String, Object>>> getAllCompetitions() {
        List<Map<String, Object>> competitions = userCompetitionService.getAllUserCompetitionsMapped();
        return ResponseEntity.ok(competitions);
    }

    @PostMapping("/userSaveStory")
    public ResponseEntity<Map<String, String>>  userSaveStory(
            @RequestBody CompetitionSubmissionsDTO competitionSubmissionsDTO) {

        String response = userCompetitionService.saveSubmitStory(competitionSubmissionsDTO);
            return ResponseEntity.ok(Map.of("message", response));
    }

    // Add this to your Controller
    @GetMapping("/user/getDraftSubmission")
    public ResponseEntity<?> getDraftSubmission(
            @RequestParam String competitionId,
            @RequestParam String email) {

        CompetitionSubmissionsDTO draft = userCompetitionService.getDraftSubmission(competitionId, email);

        if (draft != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            response.put("submission", draft);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "not_found");
            return ResponseEntity.ok(response);
        }
    }

    // Join competition
    @PostMapping("/joinCompetition")
    public ResponseEntity<Map<String, String>> joinCompetition(
            @RequestParam String competitionId,
            @RequestParam String email) {

        String response = competitionService.joinCompetition(competitionId, email);

        switch (response) {
            case "joined_successfully":
                return ResponseEntity.ok(Map.of("message", "User joined competition successfully."));
            case "competition_not_found":
                return ResponseEntity.badRequest().body(Map.of("message", "Competition not found."));
            default:
                return ResponseEntity.internalServerError().body(Map.of("message", response));
        }
    }

    // Leave competition
    @PostMapping("/leaveCompetition")
    public ResponseEntity<Map<String, String>> leaveCompetition(
            @RequestParam String competitionId,
            @RequestParam String email) {

        String response = competitionService.leaveCompetition(competitionId, email);

        if ("left_successfully".equals(response)) {
            return ResponseEntity.ok(Map.of("message", "User left competition successfully."));
        } else {
            return ResponseEntity.internalServerError().body(Map.of("message", response));
        }
    }


    // ✅ Endpoint to get all competitions a user is participating in
    @GetMapping("/participating/{email}")
    public ResponseEntity<List<String>> getCompetitionsByEmail(@PathVariable String email) {
        List<String> competitionIds = competitionService.getCompetitionsByEmail(email);
        if (competitionIds.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(competitionIds);
    }

    // ✅ Fetch all submissions for a user
    @GetMapping("/submissions/{email}")
    public ResponseEntity<List<CompetitionSubmissions>> getSubmissionsByEmail(@PathVariable String email) {
        List<CompetitionSubmissions> submissions = competitionService.getSubmissionsByEmail(email);
        if (submissions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(submissions);
    }

    // ✅ Endpoint: Get competitions where the user is participating
    @GetMapping("/myCompetitions")
    public ResponseEntity<?> getUserParticipatingCompetitions(@RequestParam("email") String email) {
        try {
            List<Map<String, Object>> competitions = competitionService.getUserParticipatingCompetitions(email);
            if (competitions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No competitions found for email: " + email);
            }
            return ResponseEntity.ok(competitions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching competitions: " + e.getMessage());
        }
    }

//    @GetMapping("/getLoginedUser")
//    public ResponseEntity<Users> getLoginedUser(@RequestParam String email) {
//        Optional<Users> user = usersRepo.findByEmail(email);
//        if (user.isPresent()) {
//            Users userData = user.get();
//            userData.setPassword(null); // Exclude password from response
//            return ResponseEntity.ok(userData);
//        }
//        return ResponseEntity.notFound().build();
//    }


    @GetMapping("/getLoginUser")
    public ResponseEntity<?> getLoginedUser(@RequestParam String email) {
        Optional<Users> user = usersRepo.loginedUser(email);

        if (user.isPresent()) {
            Users userData = user.get();
            userData.setPassword(null); // hide password for security
            return ResponseEntity.ok(userData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found for email: " + email));
        }
    }


//    @PutMapping("/{transactionId}/payment")
//    public ResponseEntity<String> updatePayment(
//            @PathVariable Long transactionId,
//            @RequestBody NewTransactionDTO updateDTO) {
//        int updated = usersRepo.updatePayment(
//                transactionId,
//                updateDTO.getUserId(),
//                updateDTO.getBookId(),
//                updateDTO.getPaymentAmount(),
//                updateDTO.getPaMethodNew(),
//                updateDTO.getPaymentStatus()
//        );
//        if (updated > 0) {
//            return ResponseEntity.ok("Payment updated successfully");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PostMapping("/userTranscation")
    public ResponseEntity<Transaction> createTransaction(@RequestBody NewTransactionDTO dto) {

        // 🧱 Step 1: Create Transaction
        Transaction transaction = new Transaction();
        transaction.setType(Transaction.TransactionType.SALE);
        transaction.setStatus(dto.getStatus());
        transaction.setPaymentAmount(dto.getPaymentAmount());
        transaction.setBookId(dto.getBookId());
        transaction.setUserId(dto.getUserId());
        transaction.setPaMethodNew(dto.getPaMethodNew());

        // Get address from Users table before saving
        Optional<String> addressOpt = usersRepo.findAddressByAllUsersId(dto.getUserId().intValue());
        addressOpt.ifPresent(transaction::setDeliveryAddress);

        // Payment logic
        String paymentMethod = dto.getPaMethodNew();
        if ("card".equalsIgnoreCase(paymentMethod)) {
            transaction.setPaymentStatus(Transaction.PaymentStatus.COMPLETED);
        } else if ("cash".equalsIgnoreCase(paymentMethod)) {
            transaction.setPaymentStatus(Transaction.PaymentStatus.PENDING);
        } else {
            transaction.setPaymentStatus(dto.getPaymentStatus());
        }

        // Save new transaction (with address)
        Transaction savedTransaction = transactionService.save(transaction);

        // 🚚 Step 2: Auto-create Delivery record
        Delivery delivery = new Delivery();
        delivery.setTransactionId(savedTransaction.getTransactionId());
        delivery.setUserId(savedTransaction.getUserId());
        delivery.setValue(savedTransaction.getPaymentAmount());
        delivery.setPickupAddress(savedTransaction.getDeliveryAddress());
        delivery.setDeliveryAddress(savedTransaction.getDeliveryAddress());
        delivery.setStatus(Delivery.DeliveryStatus.PLACED);
        // Payment method mapping
        if ("cash".equalsIgnoreCase(paymentMethod)) {
            delivery.setPaymentMethod(Delivery.PaymentMethod.CASH);
        } else {
            delivery.setPaymentMethod(Delivery.PaymentMethod.CREDIT_CARD);
        }

        // 🆔 Generate tracking number (PKP + random 4 digits)
        int randomNum = (int) (Math.random() * 9000) + 1000; // 1000–9999
        delivery.setTrackingNumber("PKP" + randomNum);

        delivery.setCreatedAt(LocalDateTime.now());

        // 💾 Save Delivery
        deliveryRepository.save(delivery);


        return ResponseEntity.ok(savedTransaction);
    }



    // Simple test endpoint
    @GetMapping("/updateAddress/{userId}")
    public ResponseEntity<String> testUpdateAddress(@PathVariable int userId) {
        try {
            String result = booksService.updateTransactionDeliveryAddress(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/bids/place")
    public String placeBid(@RequestBody UserBidDTO userBidDTO) {
        return booksService.placeUserBid(userBidDTO);
    }

    // ✅ Fetch all bids for a specific book by its ID
    @GetMapping("/bidFetch/{bookId}")
    public ResponseEntity<List<User_Bid>> getBidsByBookId(@PathVariable Long bookId) {
        List<User_Bid> bids = booksService.getBidsByBookId(bookId);
        return ResponseEntity.ok(bids);
    }

    // ✅ GET: Fetch all bid history for a given bookId
    @GetMapping("/bidHistoryFetch/{bookId}")
    public ResponseEntity<List<Bid_History>> getBidHistoryByBookId(@PathVariable Long bookId) {
        List<Bid_History> bidHistoryList = booksService.getBidHistoryByBookId(bookId);

        if (bidHistoryList.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.ok(bidHistoryList);
    }

    // ✅ Update winner name by bidId
    @PutMapping("/bidHistoryApplywinner")
    public ResponseEntity<String> updateBidWinner(
            @RequestParam Long bidId,
            @RequestParam String winnerName
    ) {
        boolean success = booksService.updateBidWinner(bidId, winnerName);

        if (success) {
            return ResponseEntity.ok("✅ Bid winner updated successfully");
        } else {
            return ResponseEntity.status(404).body("❌ Bid ID not found");
        }
    }

    // Set seller mode for a user
    @PostMapping("/setSellorStatus/{userId}")
    public SellorMode setSellor(@RequestBody SellorMode sellorMode) {
        return booksService.setSellorMode(sellorMode);
    }

    // Get seller mode for a user
    @PostMapping("/getSellorStatus/{userId}")
    public SellorMode getSellor(@PathVariable int userId) {
        return booksService.getSellorMode(userId);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Create a borrow request (client sends minimal DTO)
    @PostMapping("/createBorrow")
    public ResponseEntity<UserBorrowRequestDTO> create(@RequestBody UserBorrowRequestDTO dto) {
        UserBorrowRequestDTO saved = borrowRequestService.createBorrowRequest(dto);
        return ResponseEntity.ok(saved);
    }

    // Get one by id
    @GetMapping("/requestsBorrow/{id}")
    public ResponseEntity<UserBorrowRequestDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowRequestService.getById(id));
    }

    // Requestor's requests
    @GetMapping("/requestsBorrow/by-user/{userId}")
    public ResponseEntity<List<UserBorrowRequestDTO>> getByRequestor(@PathVariable int userId) {
        return ResponseEntity.ok(borrowRequestService.getByRequestor(userId));
    }

    // Owner's incoming requests
    @GetMapping("/requestsBorrow/by-owner/{ownerId}")
    public ResponseEntity<List<UserBorrowRequestDTO>> getByOwner(@PathVariable int ownerId) {
        return ResponseEntity.ok(borrowRequestService.getByOwner(ownerId));
    }

    // Update status: APPROVED / REJECTED / COMPLETED / CANCELLED
    @PutMapping("/requestsBorrow/{id}/status")
    public ResponseEntity<UserBorrowRequestDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(borrowRequestService.updateStatus(id, status));
    }


    // Quick boolean: any status
    @GetMapping("/existsBorrow/{userId}/{bookId}")
    public ResponseEntity<Boolean> existsAny(@PathVariable int userId, @PathVariable Long bookId) {
        boolean exists = borrowRequestService.existsBorrowRequest(userId, bookId);
        return ResponseEntity.ok(exists);
        // Or JSON: return ResponseEntity.ok(Map.of("exists", exists));
    }

    // Optional: only active statuses
    @GetMapping("/existsBorrowActive/{userId}/{bookId}")
    public ResponseEntity<Boolean> existsActive(@PathVariable int userId, @PathVariable Long bookId) {
        boolean exists = borrowRequestService.existsActiveBorrowRequest(userId, bookId);
        return ResponseEntity.ok(exists);
        // Or JSON: return ResponseEntity.ok(Map.of("exists", exists));
    }

    // Requester side: fetch entries created by this user
    // Requester side: fetch entries created by this user
    @GetMapping("/borrowUser/{userId}")
    public ResponseEntity<List<UserBorrowRequest>> getRequestsByUserEntity(@PathVariable int userId) {
        return ResponseEntity.ok(borrowRequestService.getRequestsByUserEntity(userId));
    }

    // Seller/Owner side: fetch entries where this user is the owner
    @GetMapping("/borrowOwner/{ownerId}")
    public ResponseEntity<List<UserBorrowRequest>> getRequestsByOwnerEntity(@PathVariable int ownerId) {
        return ResponseEntity.ok(borrowRequestService.getRequestsByOwnerEntity(ownerId));
    }

    /// ///////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////

    @PutMapping("/approveBorrowRequest/{bookId}/{requestId}")
    public ResponseEntity<UserBorrowRequest> approve(
            @PathVariable Long bookId,
            @PathVariable Long requestId,
            @RequestParam(name = "autoRejectOthers", defaultValue = "true") boolean autoRejectOthers
    ) {
        return ResponseEntity.ok(borrowRequestService.approve(requestId, bookId, autoRejectOthers));
    }

    // Reject specific (single)
    @PutMapping("/rejectBorrowRequest/{bookId}/{requestId}")
    public ResponseEntity<UserBorrowRequest> reject(
            @PathVariable Long bookId,
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(borrowRequestService.reject(requestId, bookId));
    }

    // Delete specific
    @DeleteMapping("/deleteBorrowRequest/{bookId}/{requestId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long bookId,
            @PathVariable Long requestId
    ) {
        borrowRequestService.delete(requestId, bookId);
        return ResponseEntity.noContent().build();
    }

    /// EXCHANGE //////////////////////////////////////////////////////////////////
    /// EXCHANGE///////////////////////////////////////////////////////////////////
    @PostMapping("/createExchange")
    public ResponseEntity<Exchange_Books> createExchange(@RequestBody Exchange_BooksDTO exchangeDTO) {
        Exchange_Books exchange = exchangeService.createExchangeAndReturnEntity(exchangeDTO);
        if (exchange == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Approver not found
        }
        return ResponseEntity.ok(exchange);
    }

    // ✅ Get all books for a user by email
    @GetMapping("/exchangeGetBooks/{email}")
    public ResponseEntity<List<UserBooks>> getBooksByEmail(@PathVariable String email) {
        List<UserBooks> books = exchangeService.getAllBooksByEmail(email);
        return ResponseEntity.ok(books);
    }

    // ✅ Check if exchange exists for user and book
    @GetMapping("/exchangeCheck/{userId}/{bookId}")
    public ResponseEntity<Boolean> checkExchangeExists(
            @PathVariable int userId,
            @PathVariable Long bookId
    ) {
        boolean exists = exchangeService.checkExchangeExists(userId, bookId);
        return ResponseEntity.ok(exists);
    }

    /// /////////////////////////////////////////////////////////////////////////////////////
    // Outgoing exchanges for user
    @GetMapping("/outgoingExchange/{userId}")
    public ResponseEntity<List<Exchange_Books>> getOutgoing(@PathVariable int userId) {
        return ResponseEntity.ok(exchangeService.getOutgoingExchanges(userId));
    }

    // Incoming exchanges for approver
    @GetMapping("/incomingExchange/{userId}")
    public ResponseEntity<List<Exchange_Books>> getIncoming(@PathVariable int userId) {
        return ResponseEntity.ok(exchangeService.getIncomingExchanges(userId));
    }

    // Get single book by bookId
    @GetMapping("/getBookExchangeById/{bookId}")
    public ResponseEntity<UserBooks> getBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(exchangeService.getBookById(bookId));
    }

    // Approve exchange
    @PutMapping("/approveExchange/{exchangeId}")
    public ResponseEntity<Map<String, String>> approve(
            @PathVariable Integer exchangeId,
            @RequestParam double deliveryFee,
            @RequestParam double handlingFee
    ) {
        String result = exchangeService.approveExchange(exchangeId, deliveryFee, handlingFee);
        if ("approved".equals(result)) {
            return ResponseEntity.ok(Map.of("status", "APPROVED"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Exchange not found"));
    }

    // Reject exchange
    @PutMapping("/rejectExchange/{exchangeId}")
    public ResponseEntity<Map<String, String>> reject(
            @PathVariable Integer exchangeId,
            @RequestParam String reason
    ) {
        String result = exchangeService.rejectExchange(exchangeId, reason);
        if ("rejected".equals(result)) {
            return ResponseEntity.ok(Map.of("status", "REJECTED"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Exchange not found"));
    }


    // ✅ Single endpoint: from user_id → email → address
    @GetMapping("/getAddressAlluser/{userId}")
    public ResponseEntity<Map<String, String>> getAddressByUserId(@PathVariable int userId) {
        String address = exchangeService.getAddressByUserId(userId);
        if (address == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Address not found for this user ID"));
        }
        return ResponseEntity.ok(Map.of("address", address));
    }

    // ✅ Get address from email
    @GetMapping("/getAddressUsers/{email}")
    public ResponseEntity<Map<String, String>> getAddressByEmail(@PathVariable String email) {
        String address = exchangeService.getAddressByEmail(email);
        if (address == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Address not found"));
        }
        return ResponseEntity.ok(Map.of("address", address));
    }

}
