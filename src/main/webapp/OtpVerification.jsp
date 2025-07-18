<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Verify OTP</title>
    <style>
        body { font-family: Arial; background-color: #f0f2f5; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .otp-container { background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); width: 300px; text-align: center; }
        .otp-container input[type="text"] { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #ddd; border-radius: 4px; }
        .otp-container input[type="submit"] { width: 100%; padding: 10px; background-color: #1a73e8; color: white; border: none; border-radius: 4px; cursor: pointer; }
        .otp-container input[type="submit"]:hover { background-color: #1557b0; }
        .error { color: red; margin-bottom: 10px; }
    </style>
</head>
<body>
    <div class="otp-container">
        <% String msg = (String) request.getAttribute("msg"); %>
        <% if (msg != null) { %>
            <div class="error"><%= msg %></div>
        <% } %>
        <h2>Verify OTP</h2>
        <form action="verifyOtp" method="post">
            <input type="hidden" name="token" value="<%= request.getAttribute("token") %>">
            <label for="otp">Enter OTP:</label><br>
            <input type="text" name="otp" id="otp" required maxlength="6"><br>
            <input type="submit" value="Verify OTP">
        </form>
        <p><a href="ForgotPassword.jsp">Resend OTP</a></p>
    </div>
</body>
</html>