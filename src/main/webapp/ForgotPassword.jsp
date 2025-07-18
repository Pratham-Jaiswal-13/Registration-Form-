<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>
    <style>
        body { font-family: Arial; background-color: #f0f2f5; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .forgot-container { background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); width: 300px; text-align: center; }
        .forgot-container input[type="text"] { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #ddd; border-radius: 4px; }
        .forgot-container input[type="submit"] { width: 100%; padding: 10px; background-color: #1a73e8; color: white; border: none; border-radius: 4px; cursor: pointer; }
        .forgot-container input[type="submit"]:hover { background-color: #1557b0; }
        .error { color: red; margin-bottom: 10px; }
    </style>
</head>
<body>
    <div class="forgot-container">
        <% String msg = (String) request.getAttribute("msg"); %>
        <% if (msg != null) { %>
            <div class="error"><%= msg %></div>
        <% } %>
        <h2>Forgot Password</h2>
        <form action="forgotPassword" method="post">
            <label for="email">Email:</label><br>
            <input type="text" name="emaill" id="email" required maxlength="50"><br>
            <input type="submit" value="Send Reset Link">
        </form>
        <p><a href="Login.jsp">Back to Login</a></p>
    </div>
</body>
</html>