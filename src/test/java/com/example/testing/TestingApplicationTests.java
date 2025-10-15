package com.example.testing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import App.BookHiveApplication;

@SpringBootTest(classes = BookHiveApplication.class)
@ActiveProfiles("test")
class BookHiveApplicationTests {

    @Test
    void contextLoads() {
    }

}
