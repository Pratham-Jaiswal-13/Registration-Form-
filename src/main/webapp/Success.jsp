<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Success</title>
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
        .success-container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 300px;
            text-align: center;
        }
        .success-container h2 {
            color: #1a73e8;
            margin-bottom: 20px;
        }
        .logout-link {
            margin-top: 10px;
            font-size: 14px;
        }
        .logout-link a {
            color: #1a73e8;
            text-decoration: none;
        }
        .logout-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="success-container">
        <h2>Welcome!</h2>
        <p>You have successfully logged in.</p>
        <div class="logout-link">
            <p><a href="Login.jsp">Logout</a></p>
        </div>
    </div>
</body>
</html>