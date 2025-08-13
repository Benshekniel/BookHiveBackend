package service.Email.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import service.Email.EmailService;

import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(String to, String subject, String text, String recipientName, MultipartFile attachment) throws IOException {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // true for multipart
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            // Prepare Thymeleaf context for the HTML template
            Context context = new Context();
            context.setVariable("recipientName", recipientName != null ? recipientName : "User");
            context.setVariable("message", text);
            context.setVariable("imageCid", "logo-image"); // Content-ID for the inline image

            // Render HTML email content using Thymeleaf
            String htmlContent = templateEngine.process("email-template", context);
            helper.setText(htmlContent, true); // true for HTML

            // Embed the image
            ClassPathResource imageResource = new ClassPathResource("static/images/logo.png");
            helper.addInline("logo-image", imageResource);

            // Add attachment if provided
            if (attachment != null && !attachment.isEmpty()) {
                helper.addAttachment(attachment.getOriginalFilename(), attachment);
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IOException("Error sending email: " + e.getMessage(), e);
        }
    }


    @Override
    public void sendEmailCustom(
            String to,
            String subject,
            String text,
            String recipientName,
            MultipartFile attachment,
            String imageFolder,   // example: "emailImages"
            String imageName      // example: "approve.png"
    ) throws IOException {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            // Prepare Thymeleaf context for HTML
            Context context = new Context();
            context.setVariable("recipientName", recipientName != null ? recipientName : "User");
            context.setVariable("message", text);
            context.setVariable("imageCid", "logo-image"); // CID for inline image

            String htmlContent = templateEngine.process("email-template", context);
            helper.setText(htmlContent, true);

            // ✅ Dynamically load image from folder & name
            String imagePath = "static/" + imageFolder + "/" + imageName;
            ClassPathResource imageResource = new ClassPathResource(imagePath);
            helper.addInline("logo-image", imageResource);

            // Add attachment if provided
            if (attachment != null && !attachment.isEmpty()) {
                helper.addAttachment(attachment.getOriginalFilename(), attachment);
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IOException("Error sending email: " + e.getMessage(), e);
        }
    }

}