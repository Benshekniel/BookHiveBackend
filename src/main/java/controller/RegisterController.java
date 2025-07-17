package controller;


import model.dto.AllUsersDTO;
import model.dto.OrgDTO;
import org.springframework.http.ResponseEntity;
import service.Register.RegisterAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.SignUp.Register_OrgAccount;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private RegisterAccount registerAccount;

    @Autowired
    private Register_OrgAccount register_OrgAccount;


    @PostMapping("/register")
    public String addAccount(@RequestBody AllUsersDTO allUsersDTO) {

        String Response= registerAccount.createAccount(allUsersDTO);
        return Response;

    }

    @PostMapping("/register_Org")
    public ResponseEntity<?> addOrg(@RequestBody OrgDTO orgDTO) {

        String Response= register_OrgAccount.createOrg(orgDTO);
        if ("success".equals(Response)) {
            AllUsersDTO allUsersDTO = new AllUsersDTO();
            allUsersDTO.setEmail(orgDTO.getEmail());
            allUsersDTO.setPassword(orgDTO.getPassword());
            allUsersDTO.setRole("organization");

            Response = registerAccount.createAccount(allUsersDTO);
        }
        return ResponseEntity.ok(Map.of("message", Response));
    }
}
