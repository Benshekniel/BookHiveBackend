package service.Email;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EmailService {
    void sendEmail(String to, String subject, String text, String recipientName, MultipartFile attachment) throws IOException;
}