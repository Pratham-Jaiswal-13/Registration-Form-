package com.ramram;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/regForm")
public class inside extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String usernameRegex = "^[a-zA-Z0-9]+(_?[a-zA-Z0-9]){7,11}$";
    private static final String passwordRegex = 
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,14}$";
    
    private static final Pattern usernamePattern = Pattern.compile(usernameRegex);
    private static final Pattern passwordPattern = Pattern.compile(passwordRegex);

    private static final int MAX_USERNAME_LENGTH = 12;
    private static final int MAX_PASSWORD_LENGTH = 14;
    private static final int MAX_EMAIL_LENGTH = 50;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8"); // Explicit UTF-8 encoding

        String meranaam = req.getParameter("namee");
        String meraEmail = req.getParameter("emaill");
        String meraPass = req.getParameter("passs");
        String meraLing = req.getParameter("genderr");

        if (meranaam == null || meranaam.trim().isEmpty()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid username!! aap kuch galti kar rhe h kripya dobara prayas kare aur saare maapdando ko paar kare!!.</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
            rd.forward(req, resp);
            return;
        }
        if (meranaam.length() > MAX_USERNAME_LENGTH) {
            req.setAttribute("msg", "<h3 style='color:red'>Warning! Aree bhaiya bhot bada naam daal rhe ho (max 12 characters). Thoda chhota karo !!</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
            rd.forward(req, resp);
            return;
        }
        if (!usernamePattern.matcher(meranaam).matches()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid username!! aap kuch galti kar rhe h kripya dobara prayas kare aur saare maapdando ko paar kare!!.</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
            rd.forward(req, resp);
            return;
        }

        if (meraPass == null || meraPass.trim().isEmpty()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid password! aap kuch galti kar rhe h kripya dobara prayas kare aur saare maapdando ko paar kare!!.</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
            rd.forward(req, resp);
            return;
        }
        if (meraPass.length() > MAX_PASSWORD_LENGTH) {
            req.setAttribute("msg", "<h3 style='color:red'>Aree bhaiya bhot bada Password daal rhe ho (max 14 characters). Thoda chhota karo !!</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
            rd.forward(req, resp);
            return;
        }
        if (!passwordPattern.matcher(meraPass).matches()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid password! aap kuch galti kar rhe h kripya dobara prayas kare aur saare maapdando ko paar kare!!.</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
            rd.forward(req, resp);
            return;
        }

        if (meraEmail == null || meraEmail.trim().isEmpty()) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid email! Please enter a valid email address.</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
            rd.forward(req, resp);
            return;
        }
        if (meraEmail.length() > MAX_EMAIL_LENGTH) {
            req.setAttribute("msg", "<h3 style='color:red'>Aree bhaiya bhot bada Email daal rhe ho (max 50 characters). Thoda chhota karo !!</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
            rd.forward(req, resp);
            return;
        }
        if (!meraEmail.contains("@") || !meraEmail.contains(".")) {
            req.setAttribute("msg", "<h3 style='color:red'>Invalid email! Please enter a valid email address.</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
            rd.forward(req, resp);
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Load driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/majak_demo", "root", "prthm");
            System.out.println("✅ JDBC Connected Successfully");

            // Check if email exists
            ps = con.prepareStatement("SELECT email FROM register WHERE email = ?");
            ps.setString(1, meraEmail);
            rs = ps.executeQuery();

            if (rs.next()) {
                req.setAttribute("msg", "<h3 style='color:orange'>Email already registered! Please login instead.</h3>");
                RequestDispatcher rd = req.getRequestDispatcher("/Login.jsp");
                rd.forward(req, resp);
            } else {
                ps = con.prepareStatement("INSERT INTO register (name, email, password, gender) VALUES (?, ?, ?, ?)");
                ps.setString(1, meranaam);
                ps.setString(2, meraEmail);
                ps.setString(3, meraPass);
                ps.setString(4, meraLing);

                int count = ps.executeUpdate();
                if (count > 0) {
                    // Create session and store details
                    HttpSession session = req.getSession();
                    session.setAttribute("userName", meranaam);
                    session.setAttribute("userEmail", meraEmail);
                    session.setAttribute("userGender", meraLing);
                    // Redirect to dashboard
                    RequestDispatcher rd = req.getRequestDispatcher("/DashBoard.jsp");
                    rd.forward(req, resp);
                } else {
                    req.setAttribute("msg", "<h3 style='color:blue'>Nirasha prapt hui dobara prayas kariye</h3>");
                    RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
                    rd.forward(req, resp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ SQL Error: " + e.getMessage());
            req.setAttribute("msg", "<h3 style='color:red'>Database error: " + e.getMessage() + "</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
            rd.forward(req, resp);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("❌ JDBC Driver Error: " + e.getMessage());
            req.setAttribute("msg", "<h3 style='color:red'>Driver error: " + e.getMessage() + "</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
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