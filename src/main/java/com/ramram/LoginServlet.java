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
import javax.servlet.http.HttpSession;

@WebServlet("/loginForm")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Validation patterns (same as registration)
    private static final String passwordRegex = 
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,14}$";
    // Email regex for basic validation (can be enhanced)
    private static final String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";

    private static final Pattern passwordPattern = Pattern.compile(passwordRegex);
    private static final Pattern emailPattern = Pattern.compile(emailRegex);

    // Maximum length limits (same as registration)
    private static final int MAX_PASSWORD_LENGTH = 14;
    private static final int MAX_EMAIL_LENGTH = 50;

    // Common registration prompt message
    private static final String registerPrompt = "<p>Don't have an account? <a href='Register.jsp'>Click here to register</a></p>";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8"); // Explicit UTF-8 encoding

        String meraEmail = req.getParameter("emaill");
        String meraPass = req.getParameter("passs");

        // Email length and validation
        if (meraEmail == null || meraEmail.trim().isEmpty()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid email! Please enter a valid email address.</h3>" + registerPrompt);
            RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
            rd.forward(req, resp);
            return;
        }
        if (meraEmail.length() > MAX_EMAIL_LENGTH) {
            req.setAttribute("msg", "<h3 style='color:red'>Warning! Email too long (max 50 characters). Please shorten it and try again!!</h3>" + registerPrompt);
            RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
            rd.forward(req, resp);
            return;
        }
        if (!emailPattern.matcher(meraEmail).matches()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid email format! Please enter a valid email address.</h3>" + registerPrompt);
            RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
            rd.forward(req, resp);
            return;
        }

        // Password length and validation
        if (meraPass == null || meraPass.trim().isEmpty()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid password! Please enter a valid password.</h3>" + registerPrompt);
            RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
            rd.forward(req, resp);
            return;
        }
        if (meraPass.length() > MAX_PASSWORD_LENGTH) {
            req.setAttribute("msg", "<h3 style='color:red'>Warning! Password too long (max 14 characters). Please shorten it and try again!!</h3>" + registerPrompt);
            RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
            rd.forward(req, resp);
            return;
        }
        if (!passwordPattern.matcher(meraPass).matches()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid password format! Must include uppercase, lowercase, digit, and special character.</h3>" + registerPrompt);
            RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
            rd.forward(req, resp);
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/majak_demo", "root", "prthm");
            System.out.println("✅ JDBC Connected Successfully");

            // Check credentials
            ps = con.prepareStatement("SELECT name, email, gender FROM register WHERE email = ? AND password = ?");
            ps.setString(1, meraEmail);
            ps.setString(2, meraPass);

            rs = ps.executeQuery();
            if (rs.next()) {
                // Login successful, store user details in session
                HttpSession session = req.getSession();
                session.setAttribute("userName", rs.getString("name"));
                session.setAttribute("userEmail", rs.getString("email"));
                session.setAttribute("userGender", rs.getString("gender"));
                // Redirect to dashboard
                RequestDispatcher rd = req.getRequestDispatcher("/DashBoard.jsp");
                rd.forward(req, resp);
            } else {
                // Login failed, redirect back to login page with error message
                req.setAttribute("msg", "<h3 style='color:red'>Invalid email or password. Please try again.</h3>" + registerPrompt);
                RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
                rd.forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ SQL Error: " + e.getMessage());
            req.setAttribute("msg", "<h3 style='color:red'>Database error: " + e.getMessage() + "</h3>" + registerPrompt);
            RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
            rd.forward(req, resp);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("❌ JDBC Driver Error: " + e.getMessage());
            req.setAttribute("msg", "<h3 style='color:red'>Driver error: " + e.getMessage() + "</h3>" + registerPrompt);
            RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
            rd.forward(req, resp);
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("❌ Error closing resources: " + e.getMessage());
            }
        }
    }
}