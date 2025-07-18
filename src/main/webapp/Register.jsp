<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.nio.charset.StandardCharsets" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registration Page</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, #6e8efb, #a777e3);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            overflow: hidden;
        }
        .register-container {
            background: rgba(255, 255, 255, 0.95) url('https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGPbdhvFlm109DBK8TqaoZu0dtQ5ec5tyl1g&s') no-repeat center center;
            background-size: cover;
            background-blend-mode: overlay;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
            width: 300px;
            text-align: center;
            border: 1px solid #e0e0e0;
            animation: fadeIn 0.5s ease-in-out;
            position: relative;
            z-index: 1;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-20px); }
            to { opacity: 1; transform: translateY(0); }
        }
        .register-container h2 {
            color: #1a73e8;
            margin-bottom: 15px;
            font-size: 24px;
            position: relative;
        }
        .register-container h2::after {
            content: '';
            position: absolute;
            width: 40px;
            height: 2px;
            background: #1a73e8;
            bottom: -6px;
            left: 50%;
            transform: translateX(-50%);
            border-radius: 2px;
        }
        .register-container label {
            display: block;
            text-align: left;
            color: #333;
            font-weight: 500;
            margin-bottom: 5px;
            font-size: 14px;
        }
        .register-container input[type="text"],
        .register-container input[type="password"],
        .register-container input[type="email"] {
            width: 100%;
            padding: 10px;
            margin: 6px 0 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            box-sizing: border-box;
            font-size: 14px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
            background: rgba(255, 255, 255, 0.98);
            color: #333;
        }
        .register-container input[type="text"]:focus,
        .register-container input[type="password"]:focus,
        .register-container input[type="email"]:focus {
            border-color: #1a73e8;
            box-shadow: 0 0 4px rgba(26, 115, 232, 0.5);
            outline: none;
        }
        .register-container input[type="submit"] {
            width: 100%;
            padding: 10px;
            background: linear-gradient(90deg, #1a73e8, #4a90e2);
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            transition: background 0.3s ease, transform 0.2s ease;
        }
        .register-container input[type="submit"]:hover {
            background: linear-gradient(90deg, #1557b0, #357abd);
            transform: translateY(-2px);
        }
        .error {
            color: #dc3545;
            margin-bottom: 10px;
            font-size: 12px;
            text-align: left;
            background: rgba(255, 255, 255, 0.95);
            padding: 8px;
            border-radius: 4px;
            z-index: 10; /* Ensure error is above other elements */
            position: relative;
            top: -5px; /* Slight adjustment to prevent overlap */
        }
        .gender-group {
            text-align: left;
            margin: 12px 0;
            background: rgba(255, 255, 255, 0.95);
            padding: 8px;
            border-radius: 6px;
            z-index: 5;
        }
        .gender-group label {
            margin-right: 8px;
            color: #333;
            font-size: 14px;
        }
        .gender-group input[type="radio"] {
            margin-right: 4px;
        }
        .login-prompt {
            margin-top: 15px;
            font-size: 12px;
            color: #666;
            background: rgba(255, 255, 255, 0.95);
            padding: 5px;
            border-radius: 4px;
            z-index: 5;
        }
        .login-prompt a {
            color: #1a73e8;
            text-decoration: underline;
            transition: color 0.3s ease;
        }
        .login-prompt a:hover {
            color: #1557b0;
        }
        .footer {
            margin-top: 12px;
            font-size: 10px;
            color: #888;
            background: rgba(255, 255, 255, 0.95);
            padding: 4px;
            border-radius: 4px;
            z-index: 5;
        }
        @media (max-width: 350px) {
            .register-container {
                width: 85%;
                padding: 15px;
            }
            .register-container h2 {
                font-size: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="register-container">
        <% String msg = (String) request.getAttribute("msg");
           if (msg != null) {
               byte[] utf8Bytes = msg.getBytes(StandardCharsets.ISO_8859_1);
               String decodedMsg = new String(utf8Bytes, StandardCharsets.UTF_8);
        %>
            <div class="error"><%= decodedMsg %></div>
        <% } %>
        <h2>खुद को हमसे जोड़े</h2>
        <form action="regForm" method="post">
            <label for="name">Username:</label>
            <input type="text" name="namee" id="name" value="" required maxlength="15"><br>
            <label for="email">Email:</label>
            <input type="email" name="emaill" id="email" value="" required maxlength="50"><br>
            <label for="pass">Password:</label>
            <input type="password" name="passs" id="pass" value="" required maxlength="18"><br>
            <div class="gender-group">
                <label>Gender:</label><br>
                <input type="radio" name="genderr" id="genderr1" value="Trans" required> <label for="genderr1">Trans</label><br>
                <input type="radio" name="genderr" id="genderr2" value="Other"> <label for="genderr2">Other</label>
            </div>
            <input type="submit" value="जुड़ने के लिए यहां दबाए">
        </form>
        <div class="login-prompt">
            Already have an account? <a href="Login.jsp">Click here to login</a>
        </div>
        <div class="footer">
            © सीता🧡राम.
        </div>
    </div>
</body>
</html>