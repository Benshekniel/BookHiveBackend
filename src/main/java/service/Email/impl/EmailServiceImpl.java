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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    // ===== NEW METHODS FOR AGENT APPLICATION SYSTEM =====

    /**
     * Send application approval email with welcome message and login credentials
     */
    public void sendApplicationApprovalEmail(
            String to,
            String applicantName,
            String applicationId,
            String tempPassword,
            String vehicleType,
            LocalDateTime processedDate) throws IOException {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("🎉 Application Approved - Welcome to Our Delivery Team!");

            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("applicantName", applicantName);
            context.setVariable("applicationId", applicationId);
            context.setVariable("email", to);
            context.setVariable("tempPassword", tempPassword);
            context.setVariable("vehicleType", vehicleType);
            context.setVariable("processedDate", processedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
            context.setVariable("currentYear", LocalDateTime.now().getYear());
            context.setVariable("companyName", "DeliveryHub");
            context.setVariable("imageCid", "approval-image");

            // Use approval-specific template
            String htmlContent = templateEngine.process("agent-approval-email", context);
            helper.setText(htmlContent, true);

            // Embed approval success image
            ClassPathResource imageResource = new ClassPathResource("static/emailImages/approval-success.png");
            helper.addInline("approval-image", imageResource);

            mailSender.send(message);

            System.out.println("✅ Application approval email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send application approval email: " + e.getMessage());
            throw new IOException("Error sending application approval email: " + e.getMessage(), e);
        }
    }

    /**
     * Send application rejection email with detailed reason
     */
    public void sendApplicationRejectionEmail(
            String to,
            String applicantName,
            String applicationId,
            String rejectionReason,
            String vehicleType,
            LocalDateTime appliedDate,
            LocalDateTime processedDate) throws IOException {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Application Status Update - " + applicationId);

            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("applicantName", applicantName);
            context.setVariable("applicationId", applicationId);
            context.setVariable("rejectionReason", rejectionReason);
            context.setVariable("vehicleType", vehicleType);
            context.setVariable("appliedDate", appliedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            context.setVariable("processedDate", processedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
            context.setVariable("currentYear", LocalDateTime.now().getYear());
            context.setVariable("companyName", "DeliveryHub");
            context.setVariable("supportEmail", "support@deliveryhub.com");
            context.setVariable("imageCid", "rejection-image");

            // Use rejection-specific template
            String htmlContent = templateEngine.process("agent-rejection-email", context);
            helper.setText(htmlContent, true);

            // Embed rejection image
            ClassPathResource imageResource = new ClassPathResource("static/emailImages/application-rejected.png");
            helper.addInline("rejection-image", imageResource);

            mailSender.send(message);

            System.out.println("✅ Application rejection email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send application rejection email: " + e.getMessage());
            throw new IOException("Error sending application rejection email: " + e.getMessage(), e);
        }
    }

    /**
     * Send application received confirmation email
     */
    public void sendApplicationReceivedEmail(
            String to,
            String applicantName,
            String applicationId,
            String vehicleType,
            LocalDateTime submittedDate) throws IOException {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Application Received - " + applicationId);

            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("applicantName", applicantName);
            context.setVariable("applicationId", applicationId);
            context.setVariable("vehicleType", vehicleType);
            context.setVariable("submittedDate", submittedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
            context.setVariable("currentYear", LocalDateTime.now().getYear());
            context.setVariable("companyName", "DeliveryHub");
            context.setVariable("estimatedProcessingTime", "2-3 business days");
            context.setVariable("imageCid", "received-image");

            // Use received confirmation template
            String htmlContent = templateEngine.process("agent-application-received-email", context);
            helper.setText(htmlContent, true);

            // Embed received confirmation image
            ClassPathResource imageResource = new ClassPathResource("static/emailImages/application-received.png");
            helper.addInline("received-image", imageResource);

            mailSender.send(message);

            System.out.println("✅ Application received email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send application received email: " + e.getMessage());
            throw new IOException("Error sending application received email: " + e.getMessage(), e);
        }
    }

    /**
     * Send document upload reminder email
     */
    public void sendDocumentReminderEmail(
            String to,
            String applicantName,
            String applicationId,
            String missingDocuments) throws IOException {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Action Required: Complete Your Application - " + applicationId);

            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("applicantName", applicantName);
            context.setVariable("applicationId", applicationId);
            context.setVariable("missingDocuments", missingDocuments);
            context.setVariable("currentYear", LocalDateTime.now().getYear());
            context.setVariable("companyName", "DeliveryHub");
            context.setVariable("reminderDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            context.setVariable("imageCid", "reminder-image");

            // Use document reminder template
            String htmlContent = templateEngine.process("agent-document-reminder-email", context);
            helper.setText(htmlContent, true);

            // Embed reminder image
            ClassPathResource imageResource = new ClassPathResource("static/emailImages/document-reminder.png");
            helper.addInline("reminder-image", imageResource);

            mailSender.send(message);

            System.out.println("✅ Document reminder email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send document reminder email: " + e.getMessage());
            throw new IOException("Error sending document reminder email: " + e.getMessage(), e);
        }
    }

    /**
     * Send welcome email after successful agent onboarding
     */
    public void sendAgentWelcomeEmail(
            String to,
            String agentName,
            String agentId,
            String hubName,
            String managerName,
            String managerContact) throws IOException {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("🚀 Welcome to the Team! Your Journey Begins Now");

            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("agentName", agentName);
            context.setVariable("agentId", agentId);
            context.setVariable("hubName", hubName);
            context.setVariable("managerName", managerName);
            context.setVariable("managerContact", managerContact);
            context.setVariable("currentYear", LocalDateTime.now().getYear());
            context.setVariable("companyName", "DeliveryHub");
            context.setVariable("welcomeDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            context.setVariable("appDownloadLink", "https://play.google.com/store/apps/deliveryhub");
            context.setVariable("imageCid", "welcome-image");

            // Use welcome template
            String htmlContent = templateEngine.process("agent-welcome-email", context);
            helper.setText(htmlContent, true);

            // Embed welcome image
            ClassPathResource imageResource = new ClassPathResource("static/emailImages/agent-welcome.png");
            helper.addInline("welcome-image", imageResource);

            mailSender.send(message);

            System.out.println("✅ Agent welcome email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send agent welcome email: " + e.getMessage());
            throw new IOException("Error sending agent welcome email: " + e.getMessage(), e);
        }
    }

    /**
     * Send bulk notification email to multiple agents
     */
    public void sendBulkNotificationEmail(
            String[] recipients,
            String subject,
            String message,
            String senderName,
            String notificationType) throws IOException {

        for (String recipient : recipients) {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(recipient);
                helper.setSubject(subject);

                // Prepare Thymeleaf context
                Context context = new Context();
                context.setVariable("recipientEmail", recipient);
                context.setVariable("message", message);
                context.setVariable("senderName", senderName);
                context.setVariable("notificationType", notificationType);
                context.setVariable("currentYear", LocalDateTime.now().getYear());
                context.setVariable("companyName", "DeliveryHub");
                context.setVariable("notificationDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
                context.setVariable("imageCid", "notification-image");

                // Use bulk notification template
                String htmlContent = templateEngine.process("bulk-notification-email", context);
                helper.setText(htmlContent, true);

                // Embed notification image
                ClassPathResource imageResource = new ClassPathResource("static/emailImages/notification.png");
                helper.addInline("notification-image", imageResource);

                mailSender.send(mimeMessage);

                // Small delay to avoid overwhelming the email server
                Thread.sleep(100);

            } catch (Exception e) {
                System.err.println("❌ Failed to send bulk email to " + recipient + ": " + e.getMessage());
            }
        }

        System.out.println("✅ Bulk notification emails sent to " + recipients.length + " recipients");
    }

    /**
     * Send password reset email for agents
     */
    public void sendAgentPasswordResetEmail(
            String to,
            String agentName,
            String resetToken,
            String agentId) throws IOException {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("🔐 Password Reset Request - DeliveryHub Agent Portal");

            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("agentName", agentName);
            context.setVariable("agentId", agentId);
            context.setVariable("resetToken", resetToken);
            context.setVariable("resetLink", "https://app.deliveryhub.com/reset-password?token=" + resetToken);
            context.setVariable("expiryTime", "30 minutes");
            context.setVariable("currentYear", LocalDateTime.now().getYear());
            context.setVariable("companyName", "DeliveryHub");
            context.setVariable("requestDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
            context.setVariable("imageCid", "password-reset-image");

            // Use password reset template
            String htmlContent = templateEngine.process("agent-password-reset-email", context);
            helper.setText(htmlContent, true);

            // Embed password reset image
            ClassPathResource imageResource = new ClassPathResource("static/emailImages/password-reset.png");
            helper.addInline("password-reset-image", imageResource);

            mailSender.send(message);

            System.out.println("✅ Password reset email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send password reset email: " + e.getMessage());
            throw new IOException("Error sending password reset email: " + e.getMessage(), e);
        }
    }

    /**
     * Send agent performance report email (monthly/weekly)
     */
    public void sendAgentPerformanceReportEmail(
            String to,
            String agentName,
            String agentId,
            int deliveriesCompleted,
            double averageRating,
            double earnings,
            String reportPeriod,
            MultipartFile reportAttachment) throws IOException {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("📊 Your Performance Report - " + reportPeriod);

            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("agentName", agentName);
            context.setVariable("agentId", agentId);
            context.setVariable("deliveriesCompleted", deliveriesCompleted);
            context.setVariable("averageRating", String.format("%.1f", averageRating));
            context.setVariable("earnings", String.format("%.2f", earnings));
            context.setVariable("reportPeriod", reportPeriod);
            context.setVariable("currentYear", LocalDateTime.now().getYear());
            context.setVariable("companyName", "DeliveryHub");
            context.setVariable("reportDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            context.setVariable("imageCid", "performance-image");

            // Use performance report template
            String htmlContent = templateEngine.process("agent-performance-report-email", context);
            helper.setText(htmlContent, true);

            // Embed performance report image
            ClassPathResource imageResource = new ClassPathResource("static/emailImages/performance-report.png");
            helper.addInline("performance-image", imageResource);

            // Add report attachment if provided
            if (reportAttachment != null && !reportAttachment.isEmpty()) {
                helper.addAttachment("Performance_Report_" + reportPeriod + ".pdf", reportAttachment);
            }

            mailSender.send(message);

            System.out.println("✅ Performance report email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send performance report email: " + e.getMessage());
            throw new IOException("Error sending performance report email: " + e.getMessage(), e);
        }
    }
}