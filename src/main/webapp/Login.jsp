<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .login-container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 300px;
            text-align: center;
            position: relative;
            z-index: 1;
        }
        .login-container h2 {
            color: #1a73e8;
            margin-bottom: 20px;
        }
        .login-container input[type="text"],
        .login-container input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .login-container input[type="submit"] {
            width: 100%;
            padding: 10px;
            background-color: #1a73e8;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        .login-container input[type="submit"]:hover {
            background-color: #1557b0;
        }
        .error {
            color: red;
            margin-bottom: 10px;
            padding: 5px;
            background-color: rgba(255, 255, 255, 0.9);
            border-radius: 4px;
            z-index: 10; /* Ensure error is above other elements */
            position: relative;
            top: -5px; /* Slight adjustment to prevent overlap */
        }
        .register-prompt a {
            color: #1a73e8;
            text-decoration: underline;
        }
        .register-prompt a:hover {
            color: #1557b0;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <% String msg = (String) request.getAttribute("msg"); %>
        <% if (msg != null) { %>
            <div class="error"><%= msg %></div>
        <% } %>
        <h2>Login</h2>
        <form action="loginForm" method="post">
            <label for="email">Email:</label><br>
            <input type="text" name="emaill" id="email" required maxlength="50"><br>
            <label for="pass">Password:</label><br>
            <input type="password" name="passs" id="pass" required maxlength="14"><br>
            <input type="submit" value="Login">
        </form>
        <div class="register-prompt">
            Don't have an account? <a href="Register.jsp">Click here to register</a><br>
            <a href="ForgotPassword.jsp">Forgot Password?</a>
        </div>
    </div>
</body>
</html>