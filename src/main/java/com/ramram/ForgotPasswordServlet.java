package com.ramram;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/forgotPassword")
public class ForgotPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String meraEmail = req.getParameter("emaill");

        if (meraEmail == null || meraEmail.trim().isEmpty() || meraEmail.length() > 50) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid email! Please enter a valid email (max 50 characters).</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/ForgotPassword.jsp");
            rd.forward(req, resp);
            return;
        }
        if (!meraEmail.contains("@") || !meraEmail.contains(".")) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid email format! Please enter a valid email address.</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/ForgotPassword.jsp");
            rd.forward(req, resp);
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/majak_demo", "root", "prthm");
            System.out.println("✅ JDBC Connected Successfully");

            ps = con.prepareStatement("SELECT email FROM register WHERE email = ?");
            ps.setString(1, meraEmail);
            rs = ps.executeQuery();

            if (rs.next()) {
                String token = UUID.randomUUID().toString();
                java.util.Date expiryDate = new java.util.Date(System.currentTimeMillis() + 3600000); // 1 hour
                String otp = String.format("%06d", new Random().nextInt(1000000)); // 6-digit OTP
                java.util.Date otpExpiry = new java.util.Date(System.currentTimeMillis() + 300000); // 5-minute expiry

                ps = con.prepareStatement("UPDATE register SET reset_password_token = ?, token_expiry = ?, otp = ?, otp_expiry = ? WHERE email = ?");
                ps.setString(1, token);
                ps.setTimestamp(2, new java.sql.Timestamp(expiryDate.getTime()));
                ps.setString(3, otp);
                ps.setTimestamp(4, new java.sql.Timestamp(otpExpiry.getTime()));
                ps.setString(5, meraEmail);
                ps.executeUpdate();

                // Email sending logic hai ye 
                String host = "smtp.gmail.com";
                String username = "prathmick@gmail.com"; 
                String password = "yjzb fala nrrl jsyn"; 

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                System.out.println("Context Path: " + req.getContextPath());
                String resetLink = "http://192.168.1.21:8080" + req.getContextPath() + "/resetpassword?token=" + token;
                System.out.println("Generated Reset Link: " + resetLink);
                String htmlContent = "<html><body>" +
                                     "<p>Click the link to reset your password: <a href='" + resetLink + "'>" + resetLink + "</a></p>" +
                                     "<p>Your OTP is: <strong>" + otp + "</strong>. It expires in 5 minutes.</p>" +
                                     "<p>This link expires in 1 hour.</p>" +
                                     "</body></html>";
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(meraEmail));
                    message.setSubject("Password Reset Request");
                    message.setContent(htmlContent, "text/html");
                    Transport.send(message);
                    System.out.println("Email sent successfully to " + meraEmail + " at " + new java.util.Date());
                    req.setAttribute("msg", "<h3 style='color:green'>Password reset link and OTP sent to your email. Check your inbox!</h3>");
                } catch (MessagingException e) {
                    System.err.println("Email sending failed: " + e.getMessage() + " at " + new java.util.Date());
                    req.setAttribute("msg", "<h3 style='color:red'>Failed to send email. Try again later. Error: " + e.getMessage() + "</h3>");
                    e.printStackTrace();
                }
            } else {
                req.setAttribute("msg", "<h3 style='color:red'>Email not found! Please register first.</h3>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ SQL Error: " + e.getMessage());
            req.setAttribute("msg", "<h3 style='color:red'>Database error: " + e.getMessage() + "</h3>");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("❌ JDBC Driver Error: " + e.getMessage());
            req.setAttribute("msg", "<h3 style='color:red'>Driver error: " + e.getMessage() + "</h3>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        RequestDispatcher rd = req.getRequestDispatcher("/ForgotPassword.jsp");
        rd.forward(req, resp);
    }
}