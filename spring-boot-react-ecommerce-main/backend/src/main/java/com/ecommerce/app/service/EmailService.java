package com.ecommerce.app.service;

import com.ecommerce.app.entity.Order;
import com.ecommerce.app.entity.OrderItem;
import com.ecommerce.app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * Email service for sending order confirmations and notifications.
 * Uses Gmail SMTP for email delivery.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");

    /**
     * Send order confirmation email asynchronously
     * Email contains order details, items, and total amount
     *
     * @param user the user who placed the order
     * @param order the order entity with all details
     */
    @Async
    public void sendOrderConfirmation(User user, Order order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject("Order Confirmation - " + order.getOrderNumber());
            helper.setText(buildOrderConfirmationEmail(user, order), true);

            mailSender.send(message);

            System.out.println("Order confirmation email sent to: " + user.getEmail());
        } catch (MessagingException e) {
            System.err.println("Failed to send order confirmation email: " + e.getMessage());
            e.printStackTrace();
        } catch (MailException e) {
            System.err.println("Mail server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Build HTML email content for order confirmation
     *
     * @param user the user
     * @param order the order
     * @return HTML email content
     */
    private String buildOrderConfirmationEmail(User user, Order order) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }");
        html.append(".content { padding: 20px; background-color: #f9f9f9; }");
        html.append(".order-details { background-color: white; padding: 15px; margin: 15px 0; border-radius: 5px; }");
        html.append(".items-table { width: 100%; border-collapse: collapse; margin: 15px 0; }");
        html.append(".items-table th { background-color: #f2f2f2; padding: 10px; text-align: left; border-bottom: 2px solid #ddd; }");
        html.append(".items-table td { padding: 10px; border-bottom: 1px solid #ddd; }");
        html.append(".total { font-size: 18px; font-weight: bold; text-align: right; margin-top: 15px; }");
        html.append(".footer { text-align: center; padding: 20px; color: #777; font-size: 12px; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");

        // Header
        html.append("<div class='header'>");
        html.append("<h1>✓ Order Confirmed!</h1>");
        html.append("</div>");

        // Content
        html.append("<div class='content'>");
        html.append("<p>Hi ").append(user.getUsername()).append(",</p>");
        html.append("<p>Thank you for your order! We're happy to confirm that we've received your order and it's being processed.</p>");

        // Order Details
        html.append("<div class='order-details'>");
        html.append("<h3>Order Details</h3>");
        html.append("<p><strong>Order Number:</strong> ").append(order.getOrderNumber()).append("</p>");
        html.append("<p><strong>Order Date:</strong> ").append(order.getOrderedAt().format(DATE_FORMATTER)).append("</p>");
        html.append("<p><strong>Status:</strong> ").append(order.getStatus()).append("</p>");
        html.append("</div>");

        // Items Table
        html.append("<h3>Order Items</h3>");
        html.append("<table class='items-table'>");
        html.append("<tr>");
        html.append("<th>Product</th>");
        html.append("<th>Quantity</th>");
        html.append("<th>Price</th>");
        html.append("<th>Subtotal</th>");
        html.append("</tr>");

        for (OrderItem item : order.getOrderItems()) {
            html.append("<tr>");
            html.append("<td>").append(item.getProduct().getName()).append("</td>");
            html.append("<td>").append(item.getQuantity()).append("</td>");
            html.append("<td>$").append(formatMoney(item.getPriceAtPurchase())).append("</td>");
            html.append("<td>$").append(formatMoney(item.getSubtotal())).append("</td>");
            html.append("</tr>");
        }

        html.append("</table>");

        // Total
        html.append("<div class='total'>");
        html.append("Total: $").append(formatMoney(order.getTotalAmount()));
        html.append("</div>");

        html.append("<p>We'll send you another email when your order ships.</p>");
        html.append("<p>If you have any questions, please don't hesitate to contact us.</p>");

        html.append("</div>");

        // Footer
        html.append("<div class='footer'>");
        html.append("<p>Thank you for shopping with us!</p>");
        html.append("<p>&copy; 2025 E-commerce Store. All rights reserved.</p>");
        html.append("</div>");

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    /**
     * Format BigDecimal money value to 2 decimal places
     *
     * @param amount the amount to format
     * @return formatted string
     */
    private String formatMoney(BigDecimal amount) {
        return String.format("%.2f", amount);
    }
}
