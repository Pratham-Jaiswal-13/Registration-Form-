package com.ramram;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/resetpassword")
public class ResetPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String passwordRegex = 
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,14}$";

    private static final Pattern passwordPattern = Pattern.compile(passwordRegex);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        if (token == null || token.trim().isEmpty()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid or missing token!</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/OtpVerification.jsp");
            rd.forward(req, resp);
            return;
        }

        req.setAttribute("token", token);
        RequestDispatcher rd = req.getRequestDispatcher("/OtpVerification.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String token = req.getParameter("token");
        String newPass = req.getParameter("newPass");

        if (newPass == null || newPass.trim().isEmpty() || newPass.length() > 14) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid password! Max 14 characters, and must meet format requirements.</h3>");
            req.setAttribute("token", token);
            RequestDispatcher rd = req.getRequestDispatcher("/ResetPassword.jsp");
            rd.forward(req, resp);
            return;
        }
        if (!passwordPattern.matcher(newPass).matches()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid password! Must include uppercase, lowercase, digit, and special character.</h3>");
            req.setAttribute("token", token);
            RequestDispatcher rd = req.getRequestDispatcher("/ResetPassword.jsp");
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

            ps = con.prepareStatement("SELECT email FROM register WHERE reset_password_token = ? AND token_expiry > NOW()");
            ps.setString(1, token);
            rs = ps.executeQuery();

            if (rs.next()) {
                String email = rs.getString("email");
                ps = con.prepareStatement("UPDATE register SET password = ?, reset_password_token = NULL, token_expiry = NULL, otp = NULL, otp_expiry = NULL WHERE email = ?");
                ps.setString(1, newPass);
                ps.setString(2, email);
                int count = ps.executeUpdate();

                if (count > 0) {
                    req.setAttribute("msg", "<h3 style='color:green'>Password reset successful! Please <a href='Login.jsp'>login</a> with your new password.</h3>");
                } else {
                    req.setAttribute("msg", "<h3 style='color:red'>Failed to reset password. Try again.</h3>");
                }
            } else {
                req.setAttribute("msg", "<h3 style='color:red'>Invalid or expired token! Request a new reset link.</h3>");
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

        RequestDispatcher rd = req.getRequestDispatcher("/ResetPassword.jsp");
        rd.forward(req, resp);
    }
}