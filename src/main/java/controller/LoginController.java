package controller;

import model.dto.LoginDto;
import model.messageResponse.LoginResponse;
import service.Login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> loginCheck(@RequestBody LoginDto loginDto)
    {
        LoginResponse loginMResponse = loginService.loginResponse(loginDto);
        return ResponseEntity.ok(loginMResponse);
    }
}
