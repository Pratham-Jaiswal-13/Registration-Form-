
package com.ramram;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/verifyOtp")
public class OtpVerificationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String token = req.getParameter("token");
        String enteredOtp = req.getParameter("otp");

        if (token == null || token.trim().isEmpty() || enteredOtp == null || enteredOtp.trim().isEmpty()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid token or OTP!</h3>");
            req.setAttribute("token", token);
            RequestDispatcher rd = req.getRequestDispatcher("/OtpVerification.jsp");
            rd.forward(req, resp);
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/majak_demo", "root", "prthm");
            ps = con.prepareStatement("SELECT otp, otp_expiry FROM register WHERE reset_password_token = ? AND token_expiry > NOW()");
            ps.setString(1, token);
            rs = ps.executeQuery();

            if (rs.next()) {
                String storedOtp = rs.getString("otp");
                java.sql.Timestamp otpExpiry = rs.getTimestamp("otp_expiry");

                if (storedOtp != null && storedOtp.equals(enteredOtp) && otpExpiry.after(new java.sql.Timestamp(System.currentTimeMillis()))) {
                    req.setAttribute("token", token);
                    RequestDispatcher rd = req.getRequestDispatcher("/ResetPassword.jsp");
                    rd.forward(req, resp);
                } else {
                    req.setAttribute("msg", "<h3 style='color:red'>Invalid or expired OTP! Request a new reset link.</h3>");
                    req.setAttribute("token", token);
                    RequestDispatcher rd = req.getRequestDispatcher("/OtpVerification.jsp");
                    rd.forward(req, resp);
                }
            } else {
                req.setAttribute("msg", "<h3 style='color:red'>Invalid token! Request a new reset link.</h3>");
                req.setAttribute("token", token);
                RequestDispatcher rd = req.getRequestDispatcher("/OtpVerification.jsp");
                rd.forward(req, resp);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            req.setAttribute("msg", "<h3 style='color:red'>Database error: " + e.getMessage() + "</h3>");
            req.setAttribute("token", token);
            RequestDispatcher rd = req.getRequestDispatcher("/OtpVerification.jsp");
            rd.forward(req, resp);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}