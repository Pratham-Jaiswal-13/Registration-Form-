<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
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
        .dashboard-container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 350px;
            text-align: center;
        }
        .dashboard-container h2 {
            color: #1a73e8;
            margin-bottom: 20px;
        }
        .dashboard-container p {
            font-size: 16px;
            color: #333;
            margin: 10px 0;
        }
        .dashboard-container a {
            display: inline-block;
            padding: 10px;
            margin: 10px 5px;
            background-color: #1a73e8;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-size: 16px;
        }
        .dashboard-container a:hover {
            background-color: #1557b0;
        }
        .dashboard-container form {
            display: inline-block;
        }
        .dashboard-container input[type="submit"] {
            padding: 10px;
            background-color: #dc3545;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        .dashboard-container input[type="submit"]:hover {
            background-color: #c82333;
        }
        .error {
            color: red;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="dashboard-container">
        <% 
            String userName = (String) session.getAttribute("userName");
            String userEmail = (String) session.getAttribute("userEmail");
            String userGender = (String) session.getAttribute("userGender");
            String msg = (String) request.getAttribute("msg");
        %>
        <% if (msg != null) { %>
            <div class="error"><%= msg %></div>
        <% } %>
        <h2>Welcome, <%= userName != null ? userName : "User" %>!</h2>
        <p><strong>Email:</strong> <%= userEmail != null ? userEmail : "N/A" %></p>
        <p><strong>Gender:</strong> <%= userGender != null ? userGender : "N/A" %></p>
        <a href="UpdateProfile.jsp">Update Profile</a>
        <form action="Logout.jsp" method="post">
            <input type="submit" value="Logout">
        </form>
    </div>
</body>
</html>