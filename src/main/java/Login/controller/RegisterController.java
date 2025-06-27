package Login.controller;


import Login.dto.AllUsersDTO;
import Login.service.RegisterAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private RegisterAccount registerAccount;

    @PostMapping("/register")
    public String addAccount(@RequestBody AllUsersDTO allUsersDTO) {

        String Response= registerAccount.createAccount(allUsersDTO);
        return Response;

    }
}
