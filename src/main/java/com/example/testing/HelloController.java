package com.example.testing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "http://localhost:9999") // Allow Vite's port
public class HelloController {

    @GetMapping("/api/message")
    public String sendMessage() {
        String messages = "Hello from Spring Boot to React!";
        return messages;
    }
}
